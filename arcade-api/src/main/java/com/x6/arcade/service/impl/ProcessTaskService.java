package com.x6.arcade.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.x6.arcade.dao.ProcessInstanceMapper;
import com.x6.arcade.dao.ProcessTaskMapper;
import com.x6.arcade.entity.ProcessInstance;
import com.x6.arcade.entity.ProcessTask;
import com.x6.arcade.entity.ProcessTaskInfo;
import com.x6.arcade.response.ProcessTaskListRequest;
import com.x6.arcade.service.IProcessTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProcessTaskService implements IProcessTaskService {

    @Autowired
    private ProcessTaskMapper processTaskMapper;
    @Autowired
    private ProcessInstanceMapper processInstanceMapper;

    @Override
    public PageInfo<ProcessTaskInfo> getTaskList(ProcessTaskListRequest request) {
        // 根据业务线+流程实例名，查询匹配的流程实例id，返回数组
        ProcessInstance processInstance = ProcessInstance.builder()
                .businessLine(request.getBusinessLine())
                .name(request.getProcessName())
                .status(1) // 只有已发布状态才能执行任务
                .build();
        List<ProcessInstance> processInstanceList = processInstanceMapper.selectByParams(processInstance);
        if(processInstanceList.size()==0){
            return new PageInfo<>();
        }
        Map<String,Object> params = new HashMap<>();
        params.put("id",request.getTaskId());
        params.put("versionId",request.getVersionId());
        params.put("status",request.getStatus());
        params.put("createdBy",request.getCreatedBy());
        params.put("processInstanceIdList",processInstanceList.stream().map(ProcessInstance::getId).toArray());
        PageHelper.startPage(request.getCurrentPage(), request.getPageSize());
        List<ProcessTask> processTaskList = processTaskMapper.selectByParams(params);
        PageInfo<ProcessTask> pageInfoOne = new PageInfo(processTaskList);

        List<ProcessTaskInfo> processTaskInfoList = JSONObject.parseArray(JSONObject.toJSONString(processTaskList),ProcessTaskInfo.class);
        processTaskInfoList.forEach(processTaskInfo -> {
            ProcessInstance currentInstance = processInstanceList.stream().filter(processInstance1 -> processInstance1.getId().equals(processTaskInfo.getProcessInstanceId())).findFirst().get();
            processTaskInfo.setProcessInstanceName(currentInstance.getName());
            processTaskInfo.setBusinessLine(currentInstance.getBusinessLine());
        });
        PageInfo<ProcessTaskInfo> pageInfoTwo = new PageInfo(processTaskInfoList);
        pageInfoTwo.setTotal(pageInfoOne.getTotal());
        return pageInfoTwo;
    }
}