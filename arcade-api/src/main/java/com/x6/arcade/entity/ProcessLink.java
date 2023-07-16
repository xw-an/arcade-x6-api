package com.x6.arcade.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProcessLink {

    private Long id;

    private Long versionId;

    private Long processInstanceId;

    private String uuid;

    private Long sourceNodeId;

    private Long targetNodeId;

    private String targetPortId;

    private String sourcePortId;

    private String lineCondition;

    private Integer isDelete;

    private String createdBy;

    private Date createdTime;

    private String updatedBy;

    private Date updatedTime;
}