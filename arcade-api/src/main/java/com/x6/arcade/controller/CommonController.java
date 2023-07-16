package com.x6.arcade.controller;

import com.x6.arcade.response.*;
import com.x6.arcade.service.ICommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "公共功能接口")
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private ICommonService commonService;

    @ApiOperation(value = "查询所有分类组件", notes = "查询所有分类组件", httpMethod = "GET")
    @GetMapping("/list")
    public List<AllComponentsResponse> listTools() {
        return commonService.listTools();
    }
}