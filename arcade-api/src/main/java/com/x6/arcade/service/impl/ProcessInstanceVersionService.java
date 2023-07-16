package com.x6.arcade.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.x6.arcade.dao.*;
import com.x6.arcade.entity.ProcessInstanceParameter;
import com.x6.arcade.entity.ProcessInstanceVersion;
import com.x6.arcade.entity.ProcessLink;
import com.x6.arcade.entity.ProcessNode;
import com.x6.arcade.service.IProcessInstanceVersionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
public class ProcessInstanceVersionService implements IProcessInstanceVersionService {

    @Autowired
    private ProcessInstanceVersionMapper processInstanceVersionMapper;
    @Autowired
    private ProcessNodeMapper processNodeMapper;
    @Autowired
    private ProcessLinkMapper processLinkMapper;
    @Autowired
    private ProcessInstanceParameterMapper processInstanceParameterMapper;
    @Autowired
    private ProcessInstanceVariableMapper processInstanceVariableMapper;

    @Override
    public List<ProcessInstanceVersion> processInstanceVersionList(Long processInstanceId) {
        return processInstanceVersionMapper.selectByProcessInstanceId(processInstanceId);
    }

    @Transactional
    @Override
    public void updateVersionStatus(ProcessInstanceVersion processInstanceVersion,Integer status) {
        List<ProcessInstanceVersion> processInstanceVersions = processInstanceVersionMapper.selectByProcessInstanceId(processInstanceVersion.getProcessInstanceId());
        // 如果是发布版本，需要判断当前流程是否有发布版本，如果有，需要将之前的发布版本状态改为下线
        if (status == 1) {
            processInstanceVersions.stream().filter(piv->piv.getId()!=processInstanceVersion.getId()&&piv.getStatus()==1).forEach(piv->{
                piv.setStatus(2);
                processInstanceVersionMapper.updateByPrimaryKeySelective(piv);
            });
        } else if (status == 2){
            // 下线版本的时候，需要判断当前流程是否有发布/新建的版本，如果没有，则不能下线
            AtomicBoolean flag = new AtomicBoolean(false);
            processInstanceVersions.stream().filter(piv->piv.getId()!=processInstanceVersion.getId()&&piv.getStatus()!=2).forEach(piv->{
                flag.set(true);
            });
            if (!flag.get()) {
                throw new RuntimeException("当前流程没有发布/新建的版本，不能下线");
            }
        }
        processInstanceVersion.setStatus(status);
        processInstanceVersion.setUpdatedTime(new Date());
        processInstanceVersionMapper.updateByPrimaryKeySelective(processInstanceVersion);
    }

    @Transactional
    @Override
    public Long copyVersion(ProcessInstanceVersion processInstanceVersion) {
        // 先是创建一个新的版本id，然后根据传入的版本id和流程实例id获取这个版本下所有的节点和连线数据，然后复制一份到新的版本id下
        Integer newVersion = processInstanceVersionMapper.selectMaxVersion(processInstanceVersion.getProcessInstanceId()) + 1;
        ProcessInstanceVersion newProcessInstanceVersion =  ProcessInstanceVersion.builder()
            .version(newVersion)
            .processInstanceId(processInstanceVersion.getProcessInstanceId())
            .createdBy(processInstanceVersion.getCreatedBy())
            .updatedBy(processInstanceVersion.getUpdatedBy())
            .createdTime(new Date())
            .updatedTime(new Date())
            .status(0)
            .build();
        processInstanceVersionMapper.insertSelective(newProcessInstanceVersion);
        // 复制节点
        Map<Long,Long> nodeMap = copyNode(processInstanceVersion.getId(),newProcessInstanceVersion.getId(),processInstanceVersion.getCreatedBy());
        // 复制连线
        copyLink(processInstanceVersion.getId(),newProcessInstanceVersion.getId(),processInstanceVersion.getCreatedBy(),nodeMap);
        return newProcessInstanceVersion.getId();
    }

    private Map copyNode(Long oldVersionId, Long newVersionId,String author) {
        Map<Long,Long> nodeMap = new HashMap<>();
        List<Long> newConditionNodeIds = new ArrayList<>();
        // 复制节点
        List<ProcessNode> processInstanceNodes = processNodeMapper.selectByVersionId(oldVersionId);
        processInstanceNodes.stream().forEach(processInstanceNode -> {
            Long oldNodeId = processInstanceNode.getId();
            // 复制节点 process_node
            processInstanceNode.setId(null);
            processInstanceNode.setVersionId(newVersionId);
            processInstanceNode.setUuid(UUID.randomUUID().toString());
            processInstanceNode.setCreatedTime(new Date());
            processInstanceNode.setUpdatedTime(new Date());
            processInstanceNode.setCreatedBy(author);
            processInstanceNode.setUpdatedBy(author);
            processNodeMapper.insertSelective(processInstanceNode);
            nodeMap.put(oldNodeId,processInstanceNode.getId());
            // 复制节点参数 process_instance_parameter
            processInstanceParameterMapper.selectByParams(ProcessInstanceParameter
                .builder()
                .versionId(oldVersionId)
                .processNodeId(oldNodeId)
                .isDelete(0)
                .build())
                .stream().forEach(processInstanceParameter -> {
                    processInstanceParameter.setId(null);
                    processInstanceParameter.setVersionId(newVersionId);
                    processInstanceParameter.setProcessNodeId(processInstanceNode.getId());
                    processInstanceParameter.setCreatedTime(new Date());
                    processInstanceParameter.setUpdatedTime(new Date());
                    processInstanceParameter.setCreatedBy(author);
                    processInstanceParameter.setUpdatedBy(author);
                    processInstanceParameterMapper.insertSelective(processInstanceParameter);
                    if (processInstanceNode.getType().equals("condition")&&processInstanceParameter.getParameterType().equals("参数信息")) {
                        newConditionNodeIds.add(processInstanceParameter.getId());
                    }
            });
            // 复制节点变量 process_instance_variable
            processInstanceVariableMapper.selectByProcessNodeId(oldVersionId,oldNodeId)
                .stream().forEach(processInstanceVariable -> {
                    processInstanceVariable.setId(null);
                    processInstanceVariable.setVersionId(newVersionId);
                    processInstanceVariable.setProcessNodeId(processInstanceNode.getId());
                    processInstanceVariable.setCreatedTime(new Date());
                    processInstanceVariable.setUpdatedTime(new Date());
                    processInstanceVariable.setCreatedBy(author);
                    processInstanceVariable.setUpdatedBy(author);
                    processInstanceVariableMapper.insertSelective(processInstanceVariable);
                });
        });
        newConditionNodeIds.stream().forEach(newConditionNodeId->{
            ProcessInstanceParameter processInstanceParameter = processInstanceParameterMapper.selectByPrimaryKey(newConditionNodeId);
            JSONArray oldJsonArray=JSONObject.parseObject(processInstanceParameter.getParameterValue()).getJSONArray("conditionList");
            oldJsonArray.forEach(oldJsonObject->{
                JSONObject oldJson = (JSONObject) oldJsonObject;
                oldJson.put("componentId",nodeMap.get(oldJson.getLong("componentId")));
            });
            processInstanceParameter.setParameterValue("{\"conditionList\":"+oldJsonArray.toJSONString()+"}");
            processInstanceParameterMapper.updateByPrimaryKeySelective(processInstanceParameter);
        });
        return nodeMap;
    }

    private void copyLink(Long oldVersionId, Long newVersionId,String author,Map<Long,Long> nodeMap) {
        List<ProcessLink> processInstanceLines = processLinkMapper.selectLineByVersionId(oldVersionId);
        processInstanceLines.stream().forEach(processInstanceLine -> {
            Long oldLineId = processInstanceLine.getId();
            // 复制连线 process_link
            processInstanceLine.setId(null);
            processInstanceLine.setVersionId(newVersionId);
            processInstanceLine.setUuid(UUID.randomUUID().toString());
            processInstanceLine.setSourceNodeId(nodeMap.get(processInstanceLine.getSourceNodeId()));
            processInstanceLine.setTargetNodeId(nodeMap.get(processInstanceLine.getTargetNodeId()));
            processInstanceLine.setCreatedTime(new Date());
            processInstanceLine.setUpdatedTime(new Date());
            processInstanceLine.setCreatedBy(author);
            processInstanceLine.setUpdatedBy(author);
            processLinkMapper.insertSelective(processInstanceLine);
            // 复制连线参数 process_instance_parameter
            processInstanceParameterMapper.selectByParams(ProcessInstanceParameter
                .builder()
                .versionId(oldVersionId)
                .processLinkId(oldLineId)
                .isDelete(0)
                .build())
                .stream().forEach(processInstanceParameter -> {
                    processInstanceParameter.setId(null);
                    processInstanceParameter.setVersionId(newVersionId);
                    processInstanceParameter.setProcessLinkId(processInstanceLine.getId());
                    processInstanceParameter.setCreatedTime(new Date());
                    processInstanceParameter.setUpdatedTime(new Date());
                    processInstanceParameter.setCreatedBy(author);
                    processInstanceParameter.setUpdatedBy(author);
                    processInstanceParameterMapper.insertSelective(processInstanceParameter);
                });
            });
    }

    @Override
    public boolean checkProcessInstanceVersionStatus(Long versionId) {
        ProcessInstanceVersion processInstanceVersions = processInstanceVersionMapper.selectByPrimaryKey(versionId);
        // 如果当前版本是下线/已发布状态，则不能编辑
        if (processInstanceVersions.getStatus() == 1 || processInstanceVersions.getStatus() == 2) {
            return false;
        }
        return true;
    }
}