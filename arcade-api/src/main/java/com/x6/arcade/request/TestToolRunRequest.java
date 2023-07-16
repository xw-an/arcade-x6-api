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
public class TestToolRunRequest implements Serializable {

    private Long toolId;

    private String operator;

    private String requestParam;

}
