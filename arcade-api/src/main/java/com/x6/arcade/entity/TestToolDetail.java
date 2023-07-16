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
public class TestToolDetail {
    private Long id;

    private Long toolId;

    private String className;

    private String interfaceName;

    private String requestParam;

    private String responseParam;

    private Date createTime;

    private Date updateTime;

}