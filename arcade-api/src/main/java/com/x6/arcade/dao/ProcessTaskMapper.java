package com.x6.arcade.dao;

import com.x6.arcade.entity.ProcessTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ProcessTaskMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProcessTask record);

    int insertSelective(ProcessTask record);

    ProcessTask selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProcessTask record);

    int updateByPrimaryKey(ProcessTask record);

    List<ProcessTask> selectByProcessInstanceId(@Param("versionId") Long versionId, @Param("processInstanceId") Long processInstanceId);

    Long selectLastTaskId(@Param("versionId") Long versionId,@Param("processInstanceId") Long processInstanceId);

    List<ProcessTask> selectByParams(Map<String,Object> params);
}