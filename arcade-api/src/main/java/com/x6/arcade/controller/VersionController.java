package com.x6.arcade.controller;

import com.x6.arcade.entity.ProcessInstanceVersion;
import com.x6.arcade.enums.ProcessInstanceVersionStatusEnum;
import com.x6.arcade.service.IProcessInstanceVersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "版本管理接口")
@Slf4j
@RestController
@RequestMapping("/version")
public class VersionController {

    @Autowired
    IProcessInstanceVersionService processInstanceVersionService;

    @ApiOperation(value = "版本管理", notes = "根据流程实例id查询所有的版本", httpMethod = "GET")
    @GetMapping("/queryVersions/{processInstanceId}")
    public List<ProcessInstanceVersion> queryVersions(@PathVariable("processInstanceId") Long processInstanceId) {
        return processInstanceVersionService.processInstanceVersionList(processInstanceId);
    }

    @ApiOperation(value = "发布版本", notes = "更新版本id对应的状态", httpMethod = "POST")
    @PostMapping("/publishVersion")
    public void publishVersion(@RequestBody ProcessInstanceVersion processInstanceVersion) {
        processInstanceVersionService.updateVersionStatus(processInstanceVersion, ProcessInstanceVersionStatusEnum.PUBLISHED.getStatusValue());
    }

    @ApiOperation(value = "下线版本", notes = "更新版本id对应的状态", httpMethod = "POST")
    @PostMapping("/offlineVersion")
    public void offlineVersion(@RequestBody ProcessInstanceVersion processInstanceVersion) {
        processInstanceVersionService.updateVersionStatus(processInstanceVersion,ProcessInstanceVersionStatusEnum.OFFLINE.getStatusValue());
    }

    @ApiOperation(value = "复制版本", notes = "版本id复制对应版本", httpMethod = "POST")
    @PostMapping("/copyVersion")
    public Long copyVersion(@RequestBody ProcessInstanceVersion processInstanceVersion) {
        return processInstanceVersionService.copyVersion(processInstanceVersion);
    }
}
