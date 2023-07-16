package com.x6.arcade.controller;

import com.github.pagehelper.PageInfo;
import com.x6.arcade.entity.*;
import com.x6.arcade.request.*;
import com.x6.arcade.response.*;
import com.x6.arcade.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "流程编排接口")
@Slf4j
@RestController
@RequestMapping("/flows")
public class FlowsController {

    @Autowired
    IProcessInstanceService processInstanceService;
    @Autowired
    IProcessNodeService processNodeService;
    @Autowired
    IProcessLinkService processLinkService;

    @ApiOperation(value = "流程列表", notes = "根据关键字查询流程列表", httpMethod = "POST")
    @PostMapping("/list")
    public ProcessInstanceListResponse listFlows(@RequestBody ProcessInstanceListRequest request) {
        PageInfo pageInfo = processInstanceService.listProcessInstances(request);
        ProcessInstanceListResponse response = ProcessInstanceListResponse
                .builder()
                .processInstances(pageInfo.getList())
                .total(pageInfo.getTotal())
                .build();
        return response;
    }

    @ApiOperation(value = "查询热门工具", notes = "根据分类查询热门列表", httpMethod = "GET")
    @GetMapping("/hot")
    public HotToolsListResponse listHotFlows(@RequestParam String category) {
        return HotToolsListResponse.builder()
                .category(category)
                .processInstances(processInstanceService.getHotProcessInstances(category))
                .build();
    }

    @ApiOperation(value = "新建流程", notes = "新建流程实例", httpMethod = "POST")
    @PostMapping("/createInstance")
    public Long createFlow(@RequestBody ProcessInstance request) {
        return processInstanceService.createProcessInstance(request);
    }

    @ApiOperation(value = "编辑流程", notes = "编辑流程实例", httpMethod = "POST")
    @PostMapping("/editInstance")
    public void editFlow(@RequestBody ProcessInstance request) {
        processInstanceService.editProcessInstance(request);
    }

    @ApiOperation(value = "新建节点", notes = "新建流程节点", httpMethod = "POST")
    @PostMapping("/createNode")
    public Long createNode(@RequestBody ProcessNodeDetailRequest request) {
        return processNodeService.createNode(request.getProcessNode(), request.getProcessInstanceParameter());
    }

    @ApiOperation(value = "删除节点", notes = "删除流程节点", httpMethod = "POST")
    @PostMapping("/deleteNode")
    public void deleteNode(@RequestBody ProcessNode request) {
        processNodeService.deleteNode(request);
    }

    @ApiOperation(value = "更新节点", notes = "更新流程节点", httpMethod = "POST")
    @PostMapping("/updateNode")
    public void updateNode(@RequestBody ProcessNodeDetailRequest request) {
        processNodeService.updateNode(request.getProcessNode(), request.getProcessInstanceParameter());
    }

    @ApiOperation(value = "更新节点坐标", notes = "更新节点坐标x，y", httpMethod = "POST")
    @PostMapping("/updateNodeCoordinate")
    public void updateNodeCoordinate(@RequestBody ProcessNode request) {
        processNodeService.updateNodeCoordinate(request);
    }

    @ApiOperation(value = "更新节点大小", notes = "更新节点大小宽，长", httpMethod = "POST")
    @PostMapping("/updateNodeSize")
    public void updateNodeSize(@RequestBody ProcessNode request) {
        processNodeService.updateNodeSize(request);
    }

    @ApiOperation(value = "新建连接线", notes = "新建连接线", httpMethod = "POST")
    @PostMapping("/createLink")
    public Long createLine(@RequestBody ProcessLinkDetailRequest request) {
        return processLinkService.createLink(request);
    }

    @ApiOperation(value = "删除连接线", notes = "删除连接线", httpMethod = "POST")
    @PostMapping("/deleteLink")
    public void deleteLine(@RequestBody ProcessLink request) {
        processLinkService.deleteLink(request);
    }

    @ApiOperation(value = "更新连接线", notes = "更新连接线", httpMethod = "POST")
    @PostMapping("/updateLink")
    public void updateLink(@RequestBody ProcessLinkDetailRequest request) {
        processLinkService.updateLink(request);
    }

    @ApiOperation(value = "更新连接线目标节点", notes = "更新连接线目标节点", httpMethod = "POST")
    @PostMapping("/updateLinkTarget")
    public void updateLinkTarget(@RequestBody ProcessLinkDetailRequest request) {
        processLinkService.updateLinkTarget(request);
    }

    @ApiOperation(value = "流程详情", notes = "根据流程实例ID查询流程详情", httpMethod = "GET")
    @GetMapping("/detail/{versionId}/{processInstanceId}")
    public ProcessInstanceDetailResponse getFlowDetail(@PathVariable("versionId") Long versionId,@PathVariable("processInstanceId") Long processInstanceId) {
        return processInstanceService.getProcessInstanceDetail(versionId,processInstanceId);
    }

    @ApiOperation(value = "生效流程", notes = "根据流程实例ID生效流程", httpMethod = "GET")
    @GetMapping("/enable")
    public void enableFlow(@RequestParam("processInstanceId") Long processInstanceId,@RequestParam("updatedBy") String updatedBy) {
        processInstanceService.enableProcessInstance(processInstanceId,updatedBy);
    }

    @ApiOperation(value = "失效流程", notes = "根据流程实例ID失效流程", httpMethod = "GET")
    @GetMapping("/disable")
    public void disableFlow(@RequestParam("processInstanceId") Long processInstanceId,@RequestParam("updatedBy") String updatedBy) {
        processInstanceService.disableProcessInstance(processInstanceId,updatedBy);
    }

    @ApiOperation(value = "查询节点参数信息", notes = "根据流程实例ID和uuid", httpMethod = "POST")
    @PostMapping("/queryParameter")
    public ProcessNodeParamResponse queryParameter(@RequestBody ProcessNodeParamRequest request) {
        return processInstanceService.getProcessNodeParam(request.getVersionId(),request.getProcessInstanceId(), request.getProcessNodeUuid(), request.getParameterType());
    }

    @ApiOperation(value = "查询流程实例的所有节点", notes = "根据查询流程实例的所有节点", httpMethod = "GET")
    @GetMapping("/queryNodes/{versionId}/{processInstanceId}")
    public List<ProcessNode> queryNodes(@PathVariable("versionId") Long versionId,@PathVariable("processInstanceId") Long processInstanceId) {
        return processInstanceService.getProcessNodes(versionId,processInstanceId);
    }

    @ApiOperation(value = "查询流程实例中的所有变量名", notes = "根据流程实例id查询流程实例中的所有变量名", httpMethod = "GET")
    @GetMapping("/queryVariables/{versionId}/{processInstanceId}")
    public List<String> queryVariables(@PathVariable("versionId") Long versionId,@PathVariable("processInstanceId") Long processInstanceId) {
        return processInstanceService.getProcessInstanceVariables(versionId,processInstanceId);
    }
}