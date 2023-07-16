package com.x6.arcade.dao;

import com.x6.arcade.entity.TestToolDetail;

public interface TestToolDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TestToolDetail record);

    int insertSelective(TestToolDetail record);

    TestToolDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TestToolDetail record);

    int updateByPrimaryKey(TestToolDetail record);

    TestToolDetail selectByToolId(Long toolId);

    int deleteByToolId(String department);
}