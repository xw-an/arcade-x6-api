package com.x6.arcade.service;

import com.github.pagehelper.PageInfo;
import com.x6.arcade.entity.TestTool;
import com.x6.arcade.entity.TestToolRunRecord;
import com.x6.arcade.request.TestToolListRequest;
import com.x6.arcade.request.TestToolRunRequest;
import com.x6.arcade.response.TestToolDetailResponse;
import com.x6.arcade.response.TestToolRunResponse;
import java.util.List;

public interface ITestToolService {

    // 查询所有工具
    public PageInfo<TestTool> listTools(TestToolListRequest request);

    // 根据id查询工具详情
    public TestToolDetailResponse getToolDetail(Long toolId);

    // 运行工具
    public TestToolRunResponse runTool(TestToolRunRequest request);

    // 查询工具执行记录
    public PageInfo<TestToolRunRecord> getToolRunRecord(Long toolId);

    // 查询部门对应工具名列表
    public List<TestTool> getToolsName(String department);

}
