package com.x6.arcade.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.x6.arcade.dao.*;
import com.x6.arcade.entity.*;
import com.x6.arcade.enums.AsyncTaskStatus;
import com.x6.arcade.request.ExecuteFlowRequest;
import com.x6.arcade.request.ProcessInstanceListRequest;
import com.x6.arcade.response.ProcessInstanceDetailResponse;
import com.x6.arcade.response.ProcessNodeParamResponse;
import com.x6.arcade.response.ProcessTaskLogResponse;
import com.x6.arcade.service.IAsyncService;
import com.x6.arcade.service.IProcessInstanceService;
import com.x6.arcade.service.IProcessLinkService;
import com.x6.arcade.service.IProcessNodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProcessInstanceService implements IProcessInstanceService {

    @Autowired
    private ProcessInstanceMapper processInstanceMapper;
    @Autowired
    private ProcessInstanceVersionMapper processInstanceVersionMapper;
    @Autowired
    private ProcessInstanceParameterMapper processInstanceParameterMapper;
    @Autowired
    private ProcessNodeMapper processNodeMapper;
    @Autowired
    private ProcessLinkMapper processLinkMapper;
    @Autowired
    private ProcessTaskMapper processTaskMapper;
    @Autowired
    private ProcessInstanceLogMapper processInstanceLogMapper;
    @Autowired
    private ProcessInstanceVariableMapper processInstanceVariableMapper;
    @Autowired
    private IProcessNodeService processNodeService;
    @Autowired
    private IProcessLinkService processLinkService;
    @Autowired
    private IAsyncService asyncService;

    @Override
    public PageInfo<ProcessInstance> listProcessInstances(ProcessInstanceListRequest request) {
        PageHelper.startPage(request.getCurrentPage(), request.getPageSize());
        ProcessInstance processInstance = new ProcessInstance();
        if (request.getStatus()<3){
            processInstance.setStatus(request.getStatus());
        }
        if (StringUtils.isNotEmpty(request.getName())){
            processInstance.setName(request.getName());
        }
        if(StringUtils.isNotEmpty(request.getBusinessLine())) {
            processInstance.setBusinessLine(request.getBusinessLine());
        }
        return new PageInfo<>(processInstanceMapper.selectByParams(processInstance));
    }

    @Transactional
    @Override
    public Long createProcessInstance(ProcessInstance processInstance) {
        if (processInstanceMapper.selectIsExist(processInstance)>0) throw new RuntimeException("流程实例已存在");
        processInstance.setCreatedTime(new Date());
        processInstance.setUpdatedTime(new Date());
        processInstanceMapper.insertSelective(processInstance);
        // 首次创建流程实例时，创建流程实例的版本号为1,状态为未发布
        ProcessInstanceVersion processInstanceVersion = ProcessInstanceVersion.builder()
                .processInstanceId(processInstance.getId())
                .version(1)
                .status(0) // 0:新建中 1:已发布 2:已下线
                .createdTime(new Date())
                .updatedTime(new Date())
                .createdBy(processInstance.getCreatedBy())
                .updatedBy(processInstance.getUpdatedBy())
                .build();
        processInstanceVersionMapper.insertSelective(processInstanceVersion);
        // 新增一个流程实例默认创建固定的开始、结束节点
        ProcessNode startNode = ProcessNode.builder()
                .versionId(processInstanceVersion.getId())
                .processInstanceId(processInstance.getId())
                .name("开始")
                .type("start")
                .uuid(UUID.randomUUID().toString())
                .position("450,30")
                .size("80,36")
                .ports("[{\"group\":\"top\",\"id\":\""+UUID.randomUUID().toString()+"\"},{\"group\":\"right\",\"id\":\""+UUID.randomUUID().toString()+"\"},{\"group\":\"bottom\",\"id\":\""+UUID.randomUUID().toString()+"\"},{\"group\":\"left\",\"id\":\""+UUID.randomUUID().toString()+"\"}]")
                .createdTime(new Date())
                .updatedTime(new Date())
                .isDelete(0)
                .createdBy(processInstance.getCreatedBy())
                .updatedBy(processInstance.getUpdatedBy())
                .build();
        ProcessNode endNode = ProcessNode.builder()
                .versionId(processInstanceVersion.getId())
                .processInstanceId(processInstance.getId())
                .name("结束")
                .uuid(UUID.randomUUID().toString())
                .type("end")
                .position("450,500")
                .size("80,36")
                .ports("[{\"group\":\"top\",\"id\":\""+UUID.randomUUID().toString()+"\"},{\"group\":\"right\",\"id\":\""+UUID.randomUUID().toString()+"\"},{\"group\":\"bottom\",\"id\":\""+UUID.randomUUID().toString()+"\"},{\"group\":\"left\",\"id\":\""+UUID.randomUUID().toString()+"\"}]")
                .createdTime(new Date())
                .updatedTime(new Date())
                .isDelete(0)
                .createdBy(processInstance.getCreatedBy())
                .updatedBy(processInstance.getUpdatedBy())
                .build();
        processNodeMapper.insertSelective(startNode);
        processNodeMapper.insertSelective(endNode);
        return processInstance.getId();
    }

    @Transactional
    @Override
    public void editProcessInstance(ProcessInstance processInstance) {
        if(processInstanceMapper.selectByPrimaryKey(processInstance.getId())==null) throw new RuntimeException("流程实例不存在");
        processInstance.setUpdatedTime(new Date());
        processInstanceMapper.updateByPrimaryKeySelective(processInstance);
    }

    @Transactional
    @Override
    public void enableProcessInstance(Long processInstanceId,String updatedBy) {
        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setId(processInstanceId);
        processInstance.setStatus(1);
        processInstance.setUpdatedBy(updatedBy);
        processInstance.setUpdatedTime(new Date());
        processInstanceMapper.updateByPrimaryKeySelective(processInstance);
    }

    @Transactional
    @Override
    public void disableProcessInstance(Long processInstanceId,String updatedBy) {
        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setId(processInstanceId);
        processInstance.setStatus(2);
        processInstance.setUpdatedBy(updatedBy);
        processInstance.setUpdatedTime(new Date());
        processInstanceMapper.updateByPrimaryKeySelective(processInstance);
    }

    @Override
    public ProcessInstanceDetailResponse getProcessInstanceDetail(Long versionId,Long processInstanceId) {
        ProcessInstanceDetailResponse response = new ProcessInstanceDetailResponse();
        ProcessInstance processInstance = processInstanceMapper.selectByPrimaryKey(processInstanceId);
        if (processInstance == null) throw new RuntimeException("流程实例不存在");
        // 查找当前流程实例的所有版本
        List<ProcessInstanceVersion> processInstanceVersionList = processInstanceVersionMapper.selectByProcessInstanceId(processInstanceId);
        Long currentVersionId = versionId;
        if (currentVersionId==0) {
            // 优先查找已发布的版本，没有在查找所有新建中的版本找最后一次创建的那个
            ProcessInstanceVersion processInstanceVersion = processInstanceVersionList.stream().filter(processInstanceVersion1 -> processInstanceVersion1.getStatus()==1)
                    .findFirst()
                    .orElse(processInstanceVersionList.stream().filter(processInstanceVersion1 -> processInstanceVersion1.getStatus()==0)
                            .max(Comparator.comparing(ProcessInstanceVersion::getCreatedTime)).orElse(null));
            currentVersionId = processInstanceVersion.getId();
        }
        response.setProcessInstance(processInstance);
        response.setProcessInstanceVersions(processInstanceVersionList);
        response.setVersionId(currentVersionId);
        response.setProcessNodeResponse(processNodeService.getNodes(currentVersionId,processInstanceId));
        response.setProcessLinkResponse(processLinkService.getLinks(currentVersionId,processInstanceId));
        return response;
    }

    @Override
    public List<ProcessInstance> getHotProcessInstances(String category) {
        ProcessInstance processInstance = ProcessInstance
                .builder()
                .businessLine(category)
                .status(1)
                .build();
        return processInstanceMapper.selectHotProcessInstances(processInstance);
    }

    @Override
    public ProcessNodeParamResponse getProcessNodeParam(Long versionId,Long processInstanceId, String processNodeUuid, String parameterType){
        List<ProcessNode> processNodes = processNodeMapper.selectByParams(ProcessNode.builder()
            .versionId(versionId)
            .isDelete(0)
            .processInstanceId(processInstanceId)
            .uuid(processNodeUuid)
            .build());
        if (processNodes.size()==0) throw new RuntimeException("该节点不存在");
        List<ProcessInstanceParameter> processInstanceParameters = processInstanceParameterMapper.selectByParams(ProcessInstanceParameter.builder()
            .versionId(versionId)
            .isDelete(0)
            .processInstanceId(processInstanceId)
            .processNodeId(processNodes.get(0).getId())
            .parameterType(parameterType)
            .build());
        return ProcessNodeParamResponse.builder()
                .processNode(processNodes.get(0))
                .type(processNodes.get(0).getType())
                .processInstanceParameter(processInstanceParameters.size()>0?processInstanceParameters.get(0):null)
                .build();
    }

    @Override
    public List<ProcessInstance> listProcessInstancesByBusinessLine(String businessLine){
        return processInstanceMapper.selectByParams(ProcessInstance.builder()
                .businessLine(businessLine)
                .status(1)
                .build());
    }

    @Override
    public List<ProcessNode> getProcessNodes(Long versionId,Long processInstanceId) {
        return processNodeMapper.selectByParams(ProcessNode.builder()
            .versionId(versionId)
            .processInstanceId(processInstanceId)
            .isDelete(0)
            .build());
    }

    @Transactional
    @Override
    public ProcessTask executeProcessInstance(ExecuteFlowRequest request) {
        // 1. 检验流程实例内容
        Long processInstanceId = request.getProcessInstanceId();
        ProcessInstance processInstance = processInstanceMapper.selectByPrimaryKey(processInstanceId);
        if (processInstance == null) throw new RuntimeException("流程实例不存在");
        if (processInstance.getStatus() != 1) throw new RuntimeException("流程实例未启用，无法执行");
        List<ProcessNode> processNodes = processNodeMapper.selectByParams(ProcessNode.builder()
            .versionId(request.getVersionId())
            .processInstanceId(processInstanceId)
            .isDelete(0)
            .build());
        if (processNodes.size() == 0) throw new RuntimeException("流程实例未创建节点，无法执行");
        List<ProcessLink> processLinks = processLinkMapper.selectByParams(ProcessLink.builder()
            .versionId(request.getVersionId())
            .processInstanceId(processInstanceId)
            .isDelete(0)
            .build());
        if (processLinks.size() == 0) throw new RuntimeException("流程实例未创建连线，无法执行");
        // 获取开始节点
        ProcessNode startNode = processNodes.stream().filter(processNode -> processNode.getType().equals("start")).collect(Collectors.toList()).get(0);
        // 获取结束节点
        ProcessNode endNode = processNodes.stream().filter(processNode -> processNode.getType().equals("end")).collect(Collectors.toList()).get(0);
        // 获取开始节点的出口连线
        List<ProcessLink> startNodeLinks = processLinks.stream().filter(processLink -> processLink.getSourceNodeId().equals(startNode.getId())).collect(Collectors.toList());
        if (startNodeLinks.size() == 0) throw new RuntimeException("流程实例未创建开始节点出口连线,无法执行");
        // 获取结束节点的入口连线
        List<ProcessLink> endNodeLinks = processLinks.stream().filter(processLink -> processLink.getTargetNodeId().equals(endNode.getId())).collect(Collectors.toList());
        if (endNodeLinks.size() == 0) throw new RuntimeException("流程实例未创建结束节点入口连线,无法执行");
        // 获取开始节点的出口连线的目标节点
        List<ProcessNode> startNodeTargetNodes = processNodes.stream().filter(processNode -> startNodeLinks.stream().map(ProcessLink::getTargetNodeId).collect(Collectors.toList()).contains(processNode.getId())).collect(Collectors.toList());
        if (startNodeTargetNodes.size() == 0) throw new RuntimeException("流程实例未创建开始节点出口连线的目标节点,无法执行");
        // 获取结束节点的入口连线
        List<ProcessNode> endNodeSourceNodes = processNodes.stream().filter(processNode -> endNodeLinks.stream().map(ProcessLink::getSourceNodeId).collect(Collectors.toList()).contains(processNode.getId())).collect(Collectors.toList());
        if (endNodeSourceNodes.size() == 0) throw new RuntimeException("流程实例未创建结束节点入口连线的源节点,无法执行");
        // 2. 校验通过之后，插入task表，进入异步执行任务
        ProcessTask task = ProcessTask.builder()
                .versionId(request.getVersionId())
                .processInstanceId(request.getProcessInstanceId())
                .createdBy(request.getAuthor())
                .updatedBy(request.getAuthor())
                .status(AsyncTaskStatus.STARTED.getStatus())
                .build();
        processTaskMapper.insertSelective(task);
        // 异步执行流程
        asyncService.asyncExecuteInstance(task,startNodeTargetNodes.get(0),endNode);
        return task;
    }

    @Override
    public List<ProcessTaskLogResponse> getProcessTaskLogs(Long versionId,Long processInstanceId){
        List<ProcessTaskLogResponse> responses = new ArrayList<>();
        List<ProcessTask> tasks = processTaskMapper.selectByProcessInstanceId(versionId,processInstanceId);
        tasks.forEach(task -> {
            ProcessTaskLogResponse response = new ProcessTaskLogResponse();
            response.setTaskId(task.getId());
            response.setCreatedBy(task.getCreatedBy());
            response.setCreatedTime(task.getCreatedTime());
            response.setStatus(task.getStatus());
            response.setProcessInstanceLogs(processInstanceLogMapper.selectByTaskId(task.getId()));
            responses.add(response);
        });
        return responses;
    }

    @Override
    public ProcessTaskLogResponse getTaskLogs(Long taskId){
        ProcessTaskLogResponse response = ProcessTaskLogResponse.builder()
                .processInstanceLogs(processInstanceLogMapper.selectByTaskId(taskId))
                .taskId(taskId)
                .status(processTaskMapper.selectByPrimaryKey(taskId).getStatus())
                .build();
        return response;
    }

    @Override
    public Long getLastTaskId(Long versionId,Long processInstanceId){
        return processTaskMapper.selectLastTaskId(versionId,processInstanceId);
    }

    @Override
    public List<String> getProcessInstanceVariables(Long versionId,Long processInstanceId){
        // 查询有效的流程实例变量，is_delete=0
        return processInstanceVariableMapper.selectByProcessInstanceId(versionId,processInstanceId).stream().map(ProcessInstanceVariable::getVariableName).collect(Collectors.toList());
    }
}