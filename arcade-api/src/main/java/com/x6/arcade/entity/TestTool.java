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
public class TestTool {

    private Long id;

    private String toolName;

    private String description;

    private String department;

    private String businessLine;

    private String developer;

    private String tags;

    private Date createTime;

    private Date updateTime;

    private String toolType;

    private Integer status;

    private String usageRestrict;
}