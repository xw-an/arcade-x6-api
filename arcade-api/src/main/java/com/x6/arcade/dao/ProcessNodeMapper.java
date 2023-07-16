package com.x6.arcade.dao;

import com.x6.arcade.entity.ProcessNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProcessNodeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProcessNode record);

    int insertSelective(ProcessNode record);

    ProcessNode selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProcessNode record);

    int updateByPrimaryKey(ProcessNode record);

    List<ProcessNode> selectByParams(ProcessNode processNode);

    int deleteByProcessInstanceIdAndUuid(ProcessNode processNode);

    Long getIdByUuid(ProcessNode processNode);

    ProcessNode getProcessNodeByUuid(ProcessNode processNode);

    int updateByPosition(ProcessNode processNode);

    int updateBySize(ProcessNode processNode);

    ProcessNode selectTypeNode(@Param("processInstanceId") Long processInstanceId, @Param("type") String type);

    List<ProcessNode> selectByVersionId(@Param("versionId") Long versionId);

}