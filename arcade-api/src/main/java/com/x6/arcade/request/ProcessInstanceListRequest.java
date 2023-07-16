package com.x6.arcade.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProcessInstanceListRequest implements Serializable {

    private String name;

    private String businessLine;

    private int status; // 0表示新建，1表示生效，2表示删除，3表示全部

    private int pageSize = 10; //每页显示条数

    private int currentPage = 1;//当前页码
}
