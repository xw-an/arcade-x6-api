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
public class TestToolRunRecord {
    private Long id;

    private Long toolId;

    private Date startTime;

    private Date endTime;

    private String requestParam;

    private String responseParam;

    private Integer status;

    private String result;

    private String operator;

    private Date createTime;

    private Date updateTime;

}