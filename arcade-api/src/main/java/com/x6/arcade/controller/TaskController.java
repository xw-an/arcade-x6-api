package com.x6.arcade.controller;

import com.github.pagehelper.PageInfo;
import com.x6.arcade.entity.ProcessTask;
import com.x6.arcade.request.ExecuteFlowRequest;
import com.x6.arcade.response.ProcessTaskListRequest;
import com.x6.arcade.response.ProcessTaskListResponse;
import com.x6.arcade.response.ProcessTaskLogResponse;
import com.x6.arcade.service.IProcessInstanceService;
import com.x6.arcade.service.IProcessTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "任务管理接口")
@Slf4j
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    IProcessInstanceService processInstanceService;
    @Autowired
    IProcessTaskService processTaskService;

    @ApiOperation(value = "执行流程工具", notes = "根据流程实例id执行流程工具", httpMethod = "POST")
    @PostMapping("/execute")
    public ProcessTask executeFlow(@RequestBody ExecuteFlowRequest request) {
        return processInstanceService.executeProcessInstance(request);
    }

    @ApiOperation(value = "查询流程工具执行日志", notes = "根据流程实例id查询流程工具执行日志", httpMethod = "GET")
    @GetMapping("/queryLogs/{versionId}/{processInstanceId}")
    public List<ProcessTaskLogResponse> queryLogs(@PathVariable("versionId") Long versionId, @PathVariable("processInstanceId") Long processInstanceId) {
        return processInstanceService.getProcessTaskLogs(versionId,processInstanceId);
    }

    @ApiOperation(value = "查询taskId下的日志", notes = "根据taskId查询所有日志", httpMethod = "GET")
    @GetMapping("/queryTaskLogs/{taskId}")
    public ProcessTaskLogResponse getTaskLogs(@PathVariable("taskId") Long taskId) {
        return processInstanceService.getTaskLogs(taskId);
    }

    @ApiOperation(value = "查询流程工具最后一次执行日志ID", notes = "根据流程实例id查询流程工具最后一次执行日志ID", httpMethod = "GET")
    @GetMapping("/queryLastTaskId/{versionId}/{processInstanceId}")
    public Long queryLastTaskId(@PathVariable("versionId") Long versionId,@PathVariable("processInstanceId") Long processInstanceId) {
        return processInstanceService.getLastTaskId(versionId,processInstanceId);
    }

    @ApiOperation(value = "查询任务list", notes = "根据筛选条件查询任务list", httpMethod = "POST")
    @PostMapping("/queryTaskList")
    public ProcessTaskListResponse queryTaskList(@RequestBody ProcessTaskListRequest request) {
        PageInfo pageInfo = processTaskService.getTaskList(request);
        ProcessTaskListResponse response = ProcessTaskListResponse
                .builder()
                .processTaskList(pageInfo.getList())
                .total(pageInfo.getTotal())
                .build();
        return response;
    }
}