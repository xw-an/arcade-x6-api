package com.x6.arcade.service.impl;

import com.x6.arcade.dao.ProcessInstanceLogMapper;
import com.x6.arcade.entity.ProcessInstanceLog;
import com.x6.arcade.entity.ProcessTask;
import com.x6.arcade.enums.AsyncTaskStatus;
import com.x6.arcade.enums.BusinessLine;
import com.x6.arcade.enums.Department;
import com.x6.arcade.response.AllComponentsResponse;
import com.x6.arcade.service.ICommonService;
import com.x6.arcade.service.IProcessInstanceService;
import com.x6.arcade.service.ITestToolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class CommonService implements ICommonService {

    @Autowired
    private ITestToolService testToolService;
    @Autowired
    private IProcessInstanceService processInstanceService;
    @Autowired
    private ProcessInstanceLogMapper processInstanceLogMapper;

    @Override
    public List<AllComponentsResponse> listTools() {
        List<AllComponentsResponse> responseList = new ArrayList<>();
        // 添加工具组件
        AllComponentsResponse allComponentsResponse = new AllComponentsResponse();
        allComponentsResponse.setCategory("工具组件");
        List<AllComponentsResponse.Children> childrenList = new ArrayList<>();
        Arrays.stream(Department.getAllDepartmentValue()).forEach(department -> {
            AllComponentsResponse.Children children = new AllComponentsResponse.Children();
            children.setBusinessName(Department.getDepartmentName(department));
            children.setChildren(testToolService.getToolsName(department));
            childrenList.add(children);
        });
        allComponentsResponse.setChildren(childrenList);
        responseList.add(allComponentsResponse);
        // 添加流程组件
        AllComponentsResponse allComponentsResponse1 = new AllComponentsResponse();
        allComponentsResponse1.setCategory("流程组件");
        List<AllComponentsResponse.Children> childrenList1 = new ArrayList<>();
        Arrays.stream(BusinessLine.getAllBusinessLineName()).forEach(businessLine -> {
            AllComponentsResponse.Children children = new AllComponentsResponse.Children();
            children.setBusinessName(businessLine);
            children.setChildren(processInstanceService.listProcessInstancesByBusinessLine(businessLine));
            childrenList1.add(children);
        });
        allComponentsResponse1.setChildren(childrenList1);
        responseList.add(allComponentsResponse1);
        return responseList;
    }

    @Override
    public void writeLogMessage(String message, ProcessTask task){
        ProcessInstanceLog log = ProcessInstanceLog.builder()
                .versionId(task.getVersionId())
                .taskId(task.getId())
                .processInstanceId(task.getProcessInstanceId())
                .message(message)
                .processResult(AsyncTaskStatus.getAsyncTaskStatusDescription(task.getStatus()))
                .createdBy(task.getCreatedBy())
                .updatedBy(task.getUpdatedBy())
                .updatedTime(new Date())
                .createdTime(new Date())
                .build();
        processInstanceLogMapper.insertSelective(log);
    }

    @Override
    public void writeLog(Long processNodeId,String message, String processType,String processParameter,String processResult,ProcessTask task){
        ProcessInstanceLog log = ProcessInstanceLog.builder()
                .versionId(task.getVersionId())
                .processNodeId(processNodeId)
                .taskId(task.getId())
                .processInstanceId(task.getProcessInstanceId())
                .message(message)
                .processType(processType)
                .processParameter(processParameter)
                .processResult(processResult)
                .createdBy(task.getCreatedBy())
                .updatedBy(task.getUpdatedBy())
                .updatedTime(new Date())
                .createdTime(new Date())
                .build();
        processInstanceLogMapper.insertSelective(log);
    }
}
