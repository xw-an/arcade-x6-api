package com.x6.arcade.response;

import com.x6.arcade.entity.TestTool;
import com.x6.arcade.entity.TestToolDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TestToolDetailResponse implements Serializable {

    private Long toolId;

    private TestTool testTool;

    private TestToolDetail testToolDetail;

}
