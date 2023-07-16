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
public class TestToolRunResponse implements Serializable {

    private Long toolId;

    private Object responseParam;
}
