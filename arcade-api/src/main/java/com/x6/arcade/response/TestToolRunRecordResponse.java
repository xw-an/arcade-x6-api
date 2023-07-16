package com.x6.arcade.response;

import com.x6.arcade.entity.TestToolRunRecord;
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
public class TestToolRunRecordResponse implements Serializable {

    private Long toolId;

    private List<TestToolRunRecord> testToolRunRecord;

    private Long total;
}
