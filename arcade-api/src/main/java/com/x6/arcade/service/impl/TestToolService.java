package com.x6.arcade.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.x6.arcade.dao.TestToolDetailMapper;
import com.x6.arcade.dao.TestToolMapper;
import com.x6.arcade.dao.TestToolRunRecordMapper;
import com.x6.arcade.entity.TestTool;
import com.x6.arcade.entity.TestToolDetail;
import com.x6.arcade.entity.TestToolRunRecord;
import com.x6.arcade.request.TestToolListRequest;
import com.x6.arcade.request.TestToolRunRequest;
import com.x6.arcade.response.TestToolDetailResponse;
import com.x6.arcade.response.TestToolRunResponse;
import com.x6.arcade.service.ITestToolService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class TestToolService implements ITestToolService {

    @Autowired
    TestToolMapper testToolMapper;

    @Autowired
    TestToolDetailMapper testToolDetailMapper;

    @Autowired
    TestToolRunRecordMapper testToolRunRecordMapper;

    @Override
    public PageInfo<TestTool> listTools(TestToolListRequest request) {
        PageHelper.startPage(request.getCurrentPage(), request.getPageSize());
        TestTool testTool = TestTool
                .builder()
                .businessLine(request.getBusinessLine())
                .department(request.getDepartment())
                .toolName(request.getKeyword())
                .status(1)
                .build();
        return new PageInfo<>(testToolMapper.selectByParams(testTool));
    }

    @Override
    public TestToolDetailResponse getToolDetail(Long toolId){
        TestTool testTool = testToolMapper.selectByPrimaryKey(toolId);
        if (ObjectUtils.isEmpty(testTool)) {
            throw new RuntimeException("该工具不存在");
        }
        TestToolDetail testToolDetail = testToolDetailMapper.selectByToolId(toolId);
        if (ObjectUtils.isEmpty(testToolDetail)) {
            throw new RuntimeException("该工具详情不存在");
        }
        return TestToolDetailResponse
                .builder()
                .toolId(toolId)
                .testTool(testTool)
                .testToolDetail(testToolDetail)
                .build();
    }

    @Override
    @Transactional
    public TestToolRunResponse runTool(TestToolRunRequest request){
        TestTool testTool = testToolMapper.selectByPrimaryKey(request.getToolId());
        if (ObjectUtils.isEmpty(testTool)) {
            throw new RuntimeException("该工具不存在");
        }
        TestToolDetail testToolDetail = testToolDetailMapper.selectByToolId(request.getToolId());
        if (ObjectUtils.isEmpty(testToolDetail)) {
            throw new RuntimeException("该工具详情不存在");
        }
        String className = testToolDetail.getClassName();
        TestToolRunRecord testToolRunRecord = TestToolRunRecord
                .builder()
                .toolId(request.getToolId())
                .requestParam(request.getRequestParam())
                .startTime(new Date())
                .status(0)
                .result("执行中")
                .operator(request.getOperator())
                .createTime(new Date())
                .updateTime(new Date())
                .build();
        testToolRunRecordMapper.insertSelective(testToolRunRecord);
        Object result = null;
        try{
            // todo 以下代码为测试代码，需要根据自己组件调用项目情况替换对应代码逻辑
            String testJson = "{\"data\":\"{\\\"result\\\":{\\\"status\\\":1,\\\"info\\\":222},\\\"id\\\":1}\",\"code\":0}";
            result = JSONObject.parseObject(testJson);
            testToolRunRecord.setEndTime(new Date());
            testToolRunRecord.setResponseParam(result==null?null:JSONObject.toJSONString(result));
            testToolRunRecord.setStatus(1);
            testToolRunRecord.setResult("执行成功");
            testToolRunRecordMapper.updateByPrimaryKeySelective(testToolRunRecord);
        }catch (Exception e){
            testToolRunRecord.setEndTime(new Date());
            testToolRunRecord.setStatus(2);
            testToolRunRecord.setResult("执行失败");
            testToolRunRecordMapper.updateByPrimaryKeySelective(testToolRunRecord);
            throw new RuntimeException("执行失败");
        }
        return TestToolRunResponse
                .builder()
                .toolId(request.getToolId())
                .responseParam(result)
                .build();
    }


    @Override
    public PageInfo<TestToolRunRecord> getToolRunRecord(Long toolId){
        PageHelper.startPage(1, 100);
        return new PageInfo<>(testToolRunRecordMapper.selectByToolId(toolId));
    }

    @Override
    public List<TestTool> getToolsName(String department){
        return testToolMapper.selectToolsName(department);
    }
}