package com.x6.arcade.dao;

import com.x6.arcade.entity.ProcessInstanceLog;

import java.util.List;

public interface ProcessInstanceLogMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProcessInstanceLog record);

    int insertSelective(ProcessInstanceLog record);

    ProcessInstanceLog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProcessInstanceLog record);

    int updateByPrimaryKey(ProcessInstanceLog record);

    List<ProcessInstanceLog> selectByTaskId(Long taskId);
}