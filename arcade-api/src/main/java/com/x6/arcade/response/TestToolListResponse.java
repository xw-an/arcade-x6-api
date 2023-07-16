package com.x6.arcade.response;

import com.x6.arcade.entity.TestTool;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestToolListResponse implements Serializable {

    private String businessLine;

    private List<TestTool> testTools;

    private Long total;
}
