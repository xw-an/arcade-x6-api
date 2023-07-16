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
public class ProcessInstance {

    private Long id;

    private String name;

    private String description;

    private String businessLine;

    private String type;

    private Integer times;

    private Integer status;

    private String createdBy;

    private Date createdTime;

    private String updatedBy;

    private Date updatedTime;

}