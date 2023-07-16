package com.x6.arcade.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProcessTaskListRequest implements Serializable {

    private Long taskId;

    private String businessLine;

    private String processName;

    private Long versionId;

    private Integer status;

    private String createdBy;

    private int pageSize = 10; //每页显示条数

    private int currentPage = 1;//当前页码
}
