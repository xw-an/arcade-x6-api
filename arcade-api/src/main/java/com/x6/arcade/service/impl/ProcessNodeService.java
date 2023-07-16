package com.x6.arcade.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.x6.arcade.dao.ProcessInstanceParameterMapper;
import com.x6.arcade.dao.ProcessInstanceVariableMapper;
import com.x6.arcade.dao.ProcessNodeMapper;
import com.x6.arcade.entity.ProcessInstanceParameter;
import com.x6.arcade.entity.ProcessInstanceVariable;
import com.x6.arcade.entity.ProcessLink;
import com.x6.arcade.entity.ProcessNode;
import com.x6.arcade.response.ProcessNodeDetailResponse;
import com.x6.arcade.service.IProcessNodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ProcessNodeService implements IProcessNodeService {

    @Autowired
    private ProcessNodeMapper processNodeMapper;
    @Autowired
    private ProcessInstanceParameterMapper processInstanceParameterMapper;
    @Autowired
    private ProcessInstanceVariableMapper processInstanceVariableMapper;
    @Autowired
    private ProcessLinkService processLinkService;
    @Autowired
    private ProcessInstanceVersionService processInstanceVersionService;

    @Transactional
    @Override
    public Long createNode(ProcessNode processNode, List<ProcessInstanceParameter> processInstanceParameters) {
        // 查询流程实例的版本的状态，如果是已发布/已下线，则不允许创建节点
        if (!processInstanceVersionService.checkProcessInstanceVersionStatus(processNode.getVersionId())) throw new RuntimeException("当前流程版本已发布/已下线，不能创建节点");
        processNode.setCreatedTime(new Date());
        processNode.setUpdatedTime(new Date());
        switch (processNode.getName()) {
            case "开始":
                processNode.setType("start");
                break;
            case "结束":
                processNode.setType("end");
                break;
            case "工具组件":
                processNode.setType("tool");
                break;
            case "自动化组件":
                processNode.setType("auto");
                break;
            case "流程组件":
                processNode.setType("flow");
                break;
            case "提取组件":
                processNode.setType("extract");
                break;
            case "条件":
                processNode.setType("condition");
                break;
            case "数据库":
                processNode.setType("database");
                break;
        }
        processNodeMapper.insertSelective(processNode);
        processInstanceParameters.stream().forEach(processInstanceParameter -> {
            processInstanceParameter.setProcessNodeId(processNode.getId());
            processInstanceParameter.setProcessInstanceId(processNode.getProcessInstanceId());
            processInstanceParameter.setCreatedTime(new Date());
            processInstanceParameter.setUpdatedTime(new Date());
            processInstanceParameterMapper.insertSelective(processInstanceParameter);
        });
        return processNode.getId();
    }

    @Transactional
    @Override
    public void deleteNode(ProcessNode processNode) {
        // 查询流程实例的版本的状态，如果是已发布/已下线，则不允许删除节点
        if (!processInstanceVersionService.checkProcessInstanceVersionStatus(processNode.getVersionId())) throw new RuntimeException("当前流程版本已发布/已下线，不能删除节点");
        ProcessNode curNode = processNodeMapper.getProcessNodeByUuid(processNode);
        // 如果是开始或者结束节点则抛异常不能删除
        if (curNode.getType().equals("start") || curNode.getType().equals("end")) {
            throw new RuntimeException("开始或者结束节点不能删除");
        }
        processNode.setUpdatedTime(new Date());
        processNodeMapper.deleteByProcessInstanceIdAndUuid(processNode);
        // 删除节点相关的连线
        processLinkService.deleteLinkByNodeId(ProcessLink.builder()
            .versionId(processNode.getVersionId())
            .processInstanceId(processNode.getProcessInstanceId())
            .sourceNodeId(curNode.getId())
            .targetNodeId(curNode.getId())
            .updatedTime(new Date())
            .updatedBy(processNode.getUpdatedBy())
            .build());
        processInstanceParameterMapper.deleteByProcessNodeId(ProcessInstanceParameter.builder()
            .versionId(processNode.getVersionId())
            .processNodeId(curNode.getId())
            .processInstanceId(processNode.getProcessInstanceId())
            .updatedTime(new Date())
            .updatedBy(processNode.getUpdatedBy())
            .build());
        // 节点类型为工具组件时，删除全局变量表
        processInstanceVariableMapper.deleteByProcessNodeId(ProcessInstanceVariable.builder()
            .versionId(processNode.getVersionId())
            .processNodeId(curNode.getId())
            .processInstanceId(processNode.getProcessInstanceId())
            .updatedTime(new Date())
            .updatedBy(processNode.getUpdatedBy())
            .build());
    }

    @Transactional
    @Override
    public void updateNode(ProcessNode processNode,List<ProcessInstanceParameter> processInstanceParameters) {
        // 查询流程实例的版本的状态，如果是已发布/已下线，则不允许更新节点
        if (!processInstanceVersionService.checkProcessInstanceVersionStatus(processNode.getVersionId())) throw new RuntimeException("当前流程版本已发布/已下线，不能更新节点");
        ProcessNode curNode = processNodeMapper.getProcessNodeByUuid(processNode);
        curNode.setName(processNode.getName());
        curNode.setPosition(processNode.getPosition());
        curNode.setUpdatedTime(new Date());
        processNodeMapper.updateByPrimaryKeySelective(curNode);
        processInstanceParameters.stream().forEach(processInstanceParameter -> {
            processInstanceParameter.setVersionId(curNode.getVersionId());
            processInstanceParameter.setProcessNodeId(curNode.getId());
            processInstanceParameter.setProcessInstanceId(curNode.getProcessInstanceId());
            processInstanceParameter.setUpdatedTime(new Date());
            processInstanceParameterMapper.updateByParameterType(processInstanceParameter);
            if (processInstanceParameter.getParameterName().equals("params")){
                switch (curNode.getType())
                {
                    // 节点类型为工具组件时，更新全局变量表
                    case "tool":
                        saveToolVariable(curNode,processInstanceParameter);
                        break;
                    case "auto":
                        break;
                    case "flow":
                        break;
                    case "extract":
                        saveExtractVariable(curNode,processInstanceParameter);
                        break;
                    case "condition":
                        break;
                    case "database":
                        break;
                }
            }
        });
    }

    @Override
    public List<ProcessNodeDetailResponse> getNodes(Long versionId,Long processInstanceId) {
        List<ProcessNodeDetailResponse> result = new ArrayList<>();
        List<ProcessNode> processNodeList = processNodeMapper.selectByParams(ProcessNode.builder()
            .versionId(versionId)
            .processInstanceId(processInstanceId)
            .isDelete(0)
            .build());
        processNodeList.forEach(processNode -> {
            ProcessNodeDetailResponse response = new ProcessNodeDetailResponse();
            response.setProcessNode(processNode);
            response.setProcessInstanceParameters(processInstanceParameterMapper.selectByParams(ProcessInstanceParameter.builder()
                .versionId(versionId)
                .processInstanceId(processInstanceId)
                .processNodeId(processNode.getId())
                .isDelete(0)
                .build()));
            response.setNodeId(processNode.getId());
            response.setProcessInstanceId(processInstanceId);
            response.setVersionId(versionId);
            result.add(response);
        });
        return result;
    }

    @Transactional
    @Override
    public void updateNodeCoordinate(ProcessNode processNode) {
        // 查询流程实例的版本的状态，如果是已发布/已下线，则不允许更新节点坐标
        if (!processInstanceVersionService.checkProcessInstanceVersionStatus(processNode.getVersionId())) throw new RuntimeException("当前流程版本已发布/已下线，不能更新节点坐标");
        processNode.setUpdatedTime(new Date());
        processNodeMapper.updateByPosition(processNode);
    }

    @Transactional
    @Override
    public void updateNodeSize(ProcessNode processNode) {
        // 查询流程实例的版本的状态，如果是已发布/已下线，则不允许更新节点大小
        if (!processInstanceVersionService.checkProcessInstanceVersionStatus(processNode.getVersionId())) throw new RuntimeException("当前流程版本已发布/已下线，不能更新节点大小");
        processNode.setUpdatedTime(new Date());
        processNodeMapper.updateBySize(processNode);
    }

    /**
     * 保存工具组件全局变量
     * @param curNode
     * @param processInstanceParameter
     */
    private void saveToolVariable(ProcessNode curNode,ProcessInstanceParameter processInstanceParameter){
        if (StringUtils.isNotEmpty(processInstanceParameter.getParameterValue())){
            Object paramName = JSON.parseObject(processInstanceParameter.getParameterValue()).get("responseParamName");
            // 判断存在就更新，不存在就插入
            Long variableId = processInstanceVariableMapper.selectById(curNode.getVersionId(),curNode.getProcessInstanceId(),curNode.getId());
            ProcessInstanceVariable processInstanceVariable = ProcessInstanceVariable.builder()
                    .versionId(curNode.getVersionId())
                    .processInstanceId(curNode.getProcessInstanceId())
                    .processNodeId(curNode.getId())
                    .type(curNode.getType())
                    .createdBy(curNode.getCreatedBy())
                    .updatedBy(curNode.getUpdatedBy())
                    .updatedTime(new Date())
                    .isDelete(0)
                    .build();
            if (paramName == null && variableId == null){
                return;
            } else if (paramName != null && !paramName.equals("") && variableId != null){
                processInstanceVariable.setVersionId(curNode.getVersionId());
                processInstanceVariable.setVariableName(paramName.toString());
                processInstanceVariable.setId(variableId);
                processInstanceVariableMapper.updateByPrimaryKeySelective(processInstanceVariable);
            } else if(paramName.equals("") && variableId != null){
                processInstanceVariable.setVersionId(curNode.getVersionId());
                processInstanceVariable.setId(variableId);
                processInstanceVariable.setIsDelete(1);
                processInstanceVariable.setVariableName("");
                processInstanceVariableMapper.updateByPrimaryKeySelective(processInstanceVariable);
            } else {
                processInstanceVariable.setVersionId(curNode.getVersionId());
                processInstanceVariable.setVariableName(paramName.toString());
                processInstanceVariableMapper.insertSelective(processInstanceVariable);
            }
        }
    }

    /**
     * 保存提取组件全局变量
     * @param curNode
     * @param processInstanceParameter
     */
    private void saveExtractVariable(ProcessNode curNode,ProcessInstanceParameter processInstanceParameter){
        if (StringUtils.isNotEmpty(processInstanceParameter.getParameterValue())){
            Object extractList = JSON.parseObject(processInstanceParameter.getParameterValue()).get("extractList");
            if (ObjectUtils.isNotEmpty(extractList)){
                // 先删除，然后再插入新的
                JSONArray jsonArray = JSONArray.parseArray(extractList.toString());
                if (jsonArray.size() > 0){
                    processInstanceVariableMapper.deleteByProcessNodeId(ProcessInstanceVariable.builder()
                        .versionId(curNode.getVersionId())
                        .processNodeId(curNode.getId())
                        .processInstanceId(curNode.getProcessInstanceId())
                        .updatedTime(new Date())
                        .updatedBy(curNode.getUpdatedBy())
                        .build());
                }
                jsonArray.forEach(obj->{
                    JSONObject jsonObject = JSONObject.parseObject(obj.toString());
                    ProcessInstanceVariable processInstanceVariable = ProcessInstanceVariable.builder()
                        .versionId(curNode.getVersionId())
                        .processInstanceId(curNode.getProcessInstanceId())
                        .processNodeId(curNode.getId())
                        .type(curNode.getType())
                        .variableName(jsonObject.get("newVariableName").toString())
                        .createdBy(curNode.getCreatedBy())
                        .updatedBy(curNode.getUpdatedBy())
                        .updatedTime(new Date())
                        .isDelete(0)
                        .build();
                    processInstanceVariableMapper.insertSelective(processInstanceVariable);
                });
            }
        }
    }
}
