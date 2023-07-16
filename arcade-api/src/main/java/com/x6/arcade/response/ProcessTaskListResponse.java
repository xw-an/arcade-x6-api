package com.x6.arcade.response;

import com.x6.arcade.entity.ProcessTaskInfo;
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
public class ProcessTaskListResponse implements Serializable {

    private List<ProcessTaskInfo> processTaskList;

    private Long total;
}
