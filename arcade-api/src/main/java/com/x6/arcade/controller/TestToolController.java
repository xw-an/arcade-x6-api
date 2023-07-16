package com.x6.arcade.controller;

import com.github.pagehelper.PageInfo;
import com.x6.arcade.entity.TestTool;
import com.x6.arcade.request.TestToolListRequest;
import com.x6.arcade.request.TestToolRunRequest;
import com.x6.arcade.response.TestToolDetailResponse;
import com.x6.arcade.response.TestToolListResponse;
import com.x6.arcade.response.TestToolRunRecordResponse;
import com.x6.arcade.response.TestToolRunResponse;
import com.x6.arcade.service.ITestToolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "工具服务功能接口")
@Slf4j
@RestController
@RequestMapping("/tools")
public class TestToolController {

    @Autowired
    private ITestToolService testToolService;

    @ApiOperation(value = "工具名筛选列表", notes = "根据业务线查询工具名列表", httpMethod = "GET")
    @GetMapping("/name")
    public List<TestTool> listToolsName(@RequestParam String department) {
        return testToolService.getToolsName(department);
    }

    @ApiOperation(value = "工具列表", notes = "根据业务线/关键字查询工具列表", httpMethod = "POST")
    @PostMapping("/list")
    public TestToolListResponse listTools(@RequestBody TestToolListRequest request) {
        PageInfo pageInfo = testToolService.listTools(request);
        TestToolListResponse response = TestToolListResponse
                .builder()
                .businessLine(request.getBusinessLine())
                .testTools(pageInfo.getList())
                .total(pageInfo.getTotal())
                .build();
        return response;
    }

    @ApiOperation(value = "工具详情", notes = "根据工具id查询工具详情", httpMethod = "GET")
    @GetMapping("/detail")
    public TestToolDetailResponse getToolDetail(@RequestParam Long toolId) {
        return testToolService.getToolDetail(toolId);
    }

    @ApiOperation(value = "运行工具", notes = "根据工具id运行工具", httpMethod = "POST")
    @PostMapping("/run")
    public TestToolRunResponse runTool(@RequestBody TestToolRunRequest request) {
        return testToolService.runTool(request);
    }

    @ApiOperation(value="查询工具执行日志", notes="根据工具id查询工具执行日志", httpMethod = "GET")
    @GetMapping("/log")
    public TestToolRunRecordResponse getToolLog(@RequestParam Long toolId) {
        PageInfo pageInfo = testToolService.getToolRunRecord(toolId);
        TestToolRunRecordResponse response = TestToolRunRecordResponse
                .builder()
                .toolId(toolId)
                .testToolRunRecord(pageInfo.getList())
                .total(pageInfo.getTotal())
                .build();
        return response;
    }
}