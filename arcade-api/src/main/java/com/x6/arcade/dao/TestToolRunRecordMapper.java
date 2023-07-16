package com.x6.arcade.dao;

import com.x6.arcade.entity.TestToolRunRecord;

import java.util.List;

public interface TestToolRunRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TestToolRunRecord record);

    int insertSelective(TestToolRunRecord record);

    TestToolRunRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TestToolRunRecord record);

    int updateByPrimaryKey(TestToolRunRecord record);

    List<TestToolRunRecord> selectByToolId(Long toolId);
}