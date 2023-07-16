package com.x6.arcade.dao;

import com.x6.arcade.entity.ProcessInstance;

import java.util.List;

public interface ProcessInstanceMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProcessInstance record);

    int insertSelective(ProcessInstance record);

    ProcessInstance selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProcessInstance record);

    int updateByPrimaryKey(ProcessInstance record);

    List<ProcessInstance> selectByParams(ProcessInstance record);

    int selectIsExist(ProcessInstance record);

    List<ProcessInstance> selectHotProcessInstances(ProcessInstance record);

}