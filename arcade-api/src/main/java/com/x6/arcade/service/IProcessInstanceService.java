package com.x6.arcade.service;

import com.github.pagehelper.PageInfo;
import com.x6.arcade.entity.*;
import com.x6.arcade.request.ExecuteFlowRequest;
import com.x6.arcade.request.ProcessInstanceListRequest;
import com.x6.arcade.response.ProcessInstanceDetailResponse;
import com.x6.arcade.response.ProcessNodeParamResponse;
import com.x6.arcade.response.ProcessTaskLogResponse;

import java.util.List;

public interface IProcessInstanceService {

    // 根据筛选条件查询有效的流程实例
    public PageInfo<ProcessInstance> listProcessInstances(ProcessInstanceListRequest request);

    // 新建流程实例
    public Long createProcessInstance(ProcessInstance processInstance);

    // 编辑流程实例
    public void editProcessInstance(ProcessInstance processInstance);

    // 生效流程实例
    public void enableProcessInstance(Long processInstanceId,String updatedBy);

    // 失效流程实例
    public void disableProcessInstance(Long processInstanceId,String updatedBy);

    // 查询流程实例详情
    public ProcessInstanceDetailResponse getProcessInstanceDetail(Long versionId,Long processInstanceId);

    // 查询热门工具
    public List<ProcessInstance> getHotProcessInstances(String category);

    // 查询节点参数
    public ProcessNodeParamResponse getProcessNodeParam(Long versionId,Long processInstanceId, String processNodeUuid, String parameterType);

    // 根据业务线查询流程实例
    public List<ProcessInstance> listProcessInstancesByBusinessLine(String businessLine);

    // 查询流程实例下的所有节点
    public List<ProcessNode> getProcessNodes(Long versionId,Long processInstanceId);

    // 执行流程工具
    public ProcessTask executeProcessInstance(ExecuteFlowRequest request);

    // 查询流程实例下的所有日志信息
    public List<ProcessTaskLogResponse> getProcessTaskLogs(Long versionId,Long processInstanceId);

    // 查询taskId下的所有日志信息
    public ProcessTaskLogResponse getTaskLogs(Long taskId);

    // 查询流程实例下的最后一次执行日志ID
    public Long getLastTaskId(Long versionId,Long processInstanceId);

    // 查询流程实例下的所有变量名
    public List<String> getProcessInstanceVariables(Long versionId,Long processInstanceId);
}
