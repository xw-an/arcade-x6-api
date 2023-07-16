package com.x6.arcade.dao;

import com.x6.arcade.entity.ProcessInstanceVariable;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProcessInstanceVariableMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProcessInstanceVariable record);

    int insertSelective(ProcessInstanceVariable record);

    ProcessInstanceVariable selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProcessInstanceVariable record);

    int updateByPrimaryKey(ProcessInstanceVariable record);

    Long selectById(@Param("versionId") Long versionId,@Param("processInstanceId") Long processInstanceId, @Param("processNodeId") Long processNodeId);

    List<ProcessInstanceVariable> selectByProcessInstanceId(@Param("versionId") Long versionId,@Param("processInstanceId") Long processInstanceId);

    List<ProcessInstanceVariable> selectByProcessNodeId(@Param("versionId") Long versionId,@Param("processNodeId") Long processNodeId);

    int deleteByProcessNodeId(ProcessInstanceVariable record);
}