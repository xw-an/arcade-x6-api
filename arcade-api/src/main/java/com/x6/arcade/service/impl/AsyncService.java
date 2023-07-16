package com.x6.arcade.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jayway.jsonpath.JsonPath;
import com.x6.arcade.analysis.JsonPathExample;
import com.x6.arcade.config.GlobalParams;
import com.x6.arcade.dao.*;
import com.x6.arcade.entity.ProcessInstanceParameter;
import com.x6.arcade.entity.ProcessNode;
import com.x6.arcade.entity.ProcessTask;
import com.x6.arcade.enums.AsyncTaskStatus;
import com.x6.arcade.request.TestToolRunRequest;
import com.x6.arcade.response.TestToolRunResponse;
import com.x6.arcade.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class AsyncService implements IAsyncService {

    @Autowired
    ProcessInstanceMapper processInstanceMapper;
    @Autowired
    ProcessNodeMapper processNodeMapper;
    @Autowired
    ProcessInstanceParameterMapper processInstanceParameterMapper;
    @Autowired
    ProcessLinkMapper processLinkMapper;
    @Autowired
    ProcessTaskMapper processTaskMapper;
    @Autowired
    ITestToolService testToolService;
    @Autowired
    ICommonService commonService;

    /**
     * <h2>异步任务需要加上注解, 并指定使用的线程池</h2>
     * 异步任务处理事情:
     *  1. 按照步骤执行流程实例
     * */
    @Async("getAsyncExecutor")
    @Override
    public void asyncExecuteInstance(ProcessTask task, ProcessNode firstNode, ProcessNode lastNode) {
        commonService.writeLogMessage("异步流程实例id:" + task.getProcessInstanceId() + "，开始执行 taskId:" + task.getId(),task);
        // 1. 按照步骤执行流程实例
        try{
            task.setStatus(AsyncTaskStatus.RUNNING.getStatus());
            processTaskMapper.updateByPrimaryKeySelective(task);
            Long nextNodeId = executeNode(task, firstNode,lastNode);
            while (nextNodeId != -1 && nextNodeId != null) {
                // 循环执行下一个节点
                nextNodeId = executeNode(task, processNodeMapper.selectByPrimaryKey(nextNodeId),lastNode);
            }
            // 2. 执行完毕后，更新任务状态
            task.setStatus(AsyncTaskStatus.SUCCESS.getStatus());
            commonService.writeLogMessage("异步流程实例id:" + task.getProcessInstanceId() + "，执行成功 taskId:" + task.getId(),task);
        }catch (Exception e){
            // 更新任务状态
            task.setStatus(AsyncTaskStatus.FAILED.getStatus());
            commonService.writeLogMessage("异步流程实例id:" + task.getProcessInstanceId() + "，执行异常 taskId:" + task.getId() + "异常信息:" + e.getMessage(),task);
        }finally {
            // 保存任务信息
            task.setUpdatedTime(new Date());
            processTaskMapper.updateByPrimaryKeySelective(task);
            // 结束时清楚线程变量
            GlobalParams.remove();
        }
    }

    // 执行节点组件内容
    private Long executeNode(ProcessTask task, ProcessNode curNode, ProcessNode lastNode) {
        // 获取下一个节点
        Long nextNodeId = 0L;
        // 执行节点内容
        List<ProcessInstanceParameter> parameters = processInstanceParameterMapper.selectByParams(ProcessInstanceParameter.builder()
            .versionId(task.getVersionId())
            .isDelete(0)
            .processInstanceId(task.getProcessInstanceId())
            .processNodeId(curNode.getId())
            .parameterName("params")
            .build());
        commonService.writeLog(curNode.getId(),"获取当前节点执行内容"+parameters, curNode.getType()+"—"+curNode.getName(), parameters.toString(),null,task);
        if(curNode.getType().equals("end")) {
            nextNodeId = -1L;
            commonService.writeLog(curNode.getId(),"【结束组件】流程结束", curNode.getType()+"—"+curNode.getName(), null,null,task);
            return nextNodeId;
        }
        if (!CollectionUtils.isEmpty(parameters)) {
            // 执行节点内容
            Map<String, Object> params = JSON.parseObject(parameters.get(0).getParameterValue(), Map.class);
            // 判断节点类型，不同类型执行不同逻辑
            switch (curNode.getType()) {
                case "tool":
                    commonService.writeLog(curNode.getId(),"【工具组件】开始执行节点name:" + curNode.getName() + "，工具组件id:" + params.get("toolId").toString(), curNode.getType()+"—"+curNode.getName(), params.get("requestJson").toString(),null,task);
                    // 执行工具组件逻辑
                    TestToolRunRequest testToolRunRequest = TestToolRunRequest.builder()
                            .toolId(Long.parseLong(params.get("toolId").toString()))
                            .operator(task.getUpdatedBy())
                            .requestParam(JsonPathExample.replaceVariable(params.get("requestJson").toString(),GlobalParams.getParams(),task.getId()))
                            .build();
                    TestToolRunResponse response = testToolService.runTool(testToolRunRequest);
                    commonService.writeLog(curNode.getId(),"【工具组件】工具组件执行入参:" + JSON.toJSONString(testToolRunRequest), curNode.getType()+"—"+curNode.getName(), JSON.toJSONString(testToolRunRequest),null,task);
                    commonService.writeLog(curNode.getId(),"【工具组件】工具组件执行结果:" + response.getResponseParam(), curNode.getType()+"—"+curNode.getName(), JSON.toJSONString(testToolRunRequest),JSON.toJSONString(response.getResponseParam()),task);
                    // 判断变量名不为空时，保存工具执行结果到变量中
                    if (params.get("responseParamName") != null && !params.get("responseParamName").toString().equals("")){
                        GlobalParams.setParams(task.getId()+params.get("responseParamName").toString(),response.getResponseParam());
                    }
                    nextNodeId =  processLinkMapper.findTargetNodeId(curNode.getVersionId(),curNode.getId(),curNode.getProcessInstanceId());
                    if (nextNodeId == null){
                        commonService.writeLog(curNode.getId(),"【工具组件】没有找到下一个执行组件id,流程执行结束",curNode.getType()+"—"+curNode.getName(),null,null,task);
                        nextNodeId = -1L;
                    } else {
                        commonService.writeLog(curNode.getId(),"【工具组件】下一个执行组件id:" + nextNodeId,curNode.getType()+"—"+curNode.getName(),null,null,task);
                    }
                    break;
                case "condition":
                    // 执行条件组件逻辑
                    commonService.writeLog(curNode.getId(),"【条件组件】开始执行节点name:" + curNode.getName(),curNode.getType()+"—"+curNode.getName(), params.get("conditionList").toString(),null,task);
                    // 获取条件组件的参数
                    JSONArray conditionList = JSONArray.parseArray(params.get("conditionList").toString());
                    // 遍历条件组件的每个分支条件
                    for (int i = 0; i < conditionList.size(); i++) {
                        // 获取条件组件的每个分支条件
                        Map<String, Object> conditionMap = JSON.parseObject(conditionList.get(i).toString(), Map.class);
                        String json = JSON.toJSONString(GlobalParams.getParams().get(task.getId()+conditionMap.get("jsonName").toString()));
                        String expression = conditionMap.get("expression").toString();
                        String operator = conditionMap.get("operator").toString();
                        String expectedValue = conditionMap.get("expectedValue").toString();
                        // 运行表达式判断结果
                        Object actualValue = null;
                        try{
                            actualValue = JsonPathExample.parseJsonPathValue(json, expression);;
                            commonService.writeLog(curNode.getId(),"【条件组件】条件组件，表达式解析成功；json对象："+json+"，表达式:" + expression + "，解析结果:" + actualValue, curNode.getType()+"—"+curNode.getName(), json,String.valueOf(actualValue),task);
                        } catch (Exception e){
                            commonService.writeLog(curNode.getId(),"【条件组件】条件组件，表达式解析失败；json对象："+json+"，表达式:" + expression + "，解析结果:" + e.getMessage(), curNode.getType()+"—"+curNode.getName(), json,null,task);
                        }
                        boolean result = JsonPathExample.compareValues(actualValue, operator, expectedValue);
                        commonService.writeLog(curNode.getId(),"【条件组件】条件组件，分支表达式:" + actualValue+expression +operator+expectedValue+"，运行结果:" + result, curNode.getType()+"—"+curNode.getName(), json,String.valueOf(result),task);
                        if (result) {
                            // 表达式结果为true时，执行组件
                            nextNodeId = Long.parseLong(conditionMap.get("componentId").toString());
                            if (nextNodeId == null){
                                commonService.writeLog(curNode.getId(),"【条件组件】没有找到下一个执行组件id,流程执行结束",curNode.getType()+"—"+curNode.getName(),null,null,task);
                                nextNodeId = -1L;
                            } else {
                                commonService.writeLog(curNode.getId(),"【条件组件】下一个执行组件id:" + nextNodeId,curNode.getType()+"—"+curNode.getName(),null,null,task);
                            }
                            break;
                        }
                    }
                    break;
                case "extract":
                    // 执行提取组件逻辑
                    commonService.writeLog(curNode.getId(),"【提取组件】开始执行节点name:" + curNode.getName(),curNode.getType()+"—"+curNode.getName(), params.get("extractList").toString(),null,task);
                    // 获取提取组件的参数
                    JSONArray extractList = JSONArray.parseArray(params.get("extractList").toString());
                    // 遍历提取组件的每个分支条件
                    for (int i = 0; i < extractList.size(); i++) {
                        // 获取提取组件的每个分支条件
                        Map<String, Object> extractMap = JSON.parseObject(extractList.get(i).toString(), Map.class);
                        String json = JSON.toJSONString(GlobalParams.getParams().get(task.getId()+extractMap.get("oldVariableName").toString()));
                        String expression = extractMap.get("expression").toString();
                        String newVariableName = extractMap.get("newVariableName").toString();
                        // 运行表达式，将结果保存到新变量中
//                        Object actualValue = JsonPath.parse(json).read(expression);
                        Object actualValue = null;
                        try{
                            actualValue = JsonPathExample.parseJsonPathValue(json, expression);;
                            commonService.writeLog(curNode.getId(),"【提取组件】提取组件，表达式解析成功；，json对象："+json+"，表达式:" + expression + "，解析结果:" + actualValue, curNode.getType()+"—"+curNode.getName(), json,String.valueOf(actualValue),task);
                        } catch (Exception e){
                            commonService.writeLog(curNode.getId(),"【提取组件】提取组件，表达式解析失败；json对象："+json+"，表达式:" + expression + "，解析结果:" + e.getMessage(), curNode.getType()+"—"+curNode.getName(), json,null,task);
                        }
                        GlobalParams.setParams(task.getId()+newVariableName,actualValue);
                        commonService.writeLog(curNode.getId(),"【提取组件】提取组件，保存变量："+newVariableName+"，值:" + actualValue, curNode.getType()+"—"+curNode.getName(), json,String.valueOf(actualValue),task);
                    }
                    nextNodeId =  processLinkMapper.findTargetNodeId(curNode.getVersionId(),curNode.getId(),curNode.getProcessInstanceId());
                    if (nextNodeId == null){
                        commonService.writeLog(curNode.getId(),"【提取组件】没有找到下一个执行组件id,流程执行结束",curNode.getType()+"—"+curNode.getName(),null,null,task);
                        nextNodeId = -1L;
                    } else {
                        commonService.writeLog(curNode.getId(),"【提取组件】下一个执行组件id:" + nextNodeId,curNode.getType()+"—"+curNode.getName(),null,null,task);
                    }
                    break;
                default:
                    break;
                // todo 还有其他类型组件
            }
        }
        return nextNodeId;
    }
}