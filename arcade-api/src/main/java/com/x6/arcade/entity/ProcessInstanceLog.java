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
public class ProcessInstanceLog {

    private Long id;

    private Long versionId;

    private Long taskId;

    private Long processNodeId;

    private Long processInstanceId;

    private String message;

    private String processType;

    private String processParameter;

    private String processResult;

    private Date createdTime;

    private String createdBy;

    private String updatedBy;

    private Date updatedTime;

}