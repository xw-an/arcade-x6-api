package com.x6.arcade.dao;

import com.x6.arcade.entity.ProcessInstanceVersion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProcessInstanceVersionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProcessInstanceVersion record);

    int insertSelective(ProcessInstanceVersion record);

    ProcessInstanceVersion selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProcessInstanceVersion record);

    int updateByPrimaryKey(ProcessInstanceVersion record);

    List<ProcessInstanceVersion> selectByProcessInstanceId(@Param("processInstanceId") Long processInstanceId);

    Integer selectMaxVersion(@Param("processInstanceId") Long processInstanceId);
}