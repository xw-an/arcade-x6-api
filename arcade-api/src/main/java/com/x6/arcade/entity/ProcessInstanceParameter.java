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
public class ProcessInstanceParameter {

    private Long id;

    private Long versionId;

    private Long processInstanceId;

    private Long processNodeId;

    private Long processLinkId;

    private String parameterType;

    private String parameterName;

    private String parameterValue;

    private Integer isDelete;

    private String createdBy;

    private Date createdTime;

    private String updatedBy;

    private Date updatedTime;

}