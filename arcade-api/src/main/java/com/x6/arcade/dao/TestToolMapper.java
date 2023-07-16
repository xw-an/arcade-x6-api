package com.x6.arcade.dao;

import com.x6.arcade.entity.TestTool;

import java.util.List;

public interface TestToolMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TestTool record);

    int insertSelective(TestTool record);

    TestTool selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TestTool record);

    int updateByPrimaryKey(TestTool record);

    List<TestTool> selectByParams(TestTool record);

    List<TestTool> selectToolsName(String department);

    int deleteByDepartment(String department);

    List<Long> selectByDepartment(String department);
}