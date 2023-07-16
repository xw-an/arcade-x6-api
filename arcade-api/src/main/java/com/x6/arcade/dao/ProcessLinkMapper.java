package com.x6.arcade.dao;

import com.x6.arcade.entity.ProcessLink;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProcessLinkMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProcessLink record);

    int insertSelective(ProcessLink record);

    ProcessLink selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ProcessLink record);

    int updateByPrimaryKey(ProcessLink record);

    List<ProcessLink> selectByParams(ProcessLink processLink);

    List<ProcessLink> selectBySourceTargetNodeId(ProcessLink processLink);

    Long getIdByUuid(ProcessLink processLink);

    int deleteByProcessInstanceIdAndUuid(ProcessLink processLink);

    int deleteBySourceTargetNodeId(ProcessLink processLink);

    int updateLinkTarget(ProcessLink processLink);

    Long findTargetNodeId(@Param("versionId") Long versionId,@Param("sourceNodeId") Long sourceNodeId, @Param("processInstanceId") Long processInstanceId);

    List<ProcessLink> selectLineByVersionId(@Param("versionId") Long versionId);
}