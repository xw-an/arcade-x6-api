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
public class ProcessTask {

    private Long id;

    private Long versionId;

    private Long processInstanceId;

    private Integer status; // 0: 已经启动, 1: 正在运行, 2: 执行成功, 3: 执行失败

    private String createdBy;

    private Date createdTime;

    private String updatedBy;

    private Date updatedTime;

}