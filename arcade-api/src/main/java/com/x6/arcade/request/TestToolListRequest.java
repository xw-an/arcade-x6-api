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
public class TestToolListRequest implements Serializable {

    private String department;

    private String businessLine;

    private String keyword;

    private int pageSize = 10; //每页显示条数

    private int currentPage = 1;//当前页码

}
