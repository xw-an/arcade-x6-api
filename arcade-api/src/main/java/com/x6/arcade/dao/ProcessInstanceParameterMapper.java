package com.x6.arcade.dao;

import com.x6.arcade.entity.ProcessInstanceParameter;

import java.util.List;

public interface ProcessInstanceParameterMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProcessInstanceParameter record);

    int insertSelective(ProcessInstanceParameter record);

    ProcessInstanceParameter selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProcessInstanceParameter record);

    int updateByPrimaryKey(ProcessInstanceParameter record);

    List<ProcessInstanceParameter> selectByParams(ProcessInstanceParameter processInstanceParameter);

    int deleteByProcessNodeId(ProcessInstanceParameter processInstanceParameter);

    int deleteByProcessLinkId(ProcessInstanceParameter processInstanceParameter);

    int updateByParameterType(ProcessInstanceParameter processInstanceParameter);
}