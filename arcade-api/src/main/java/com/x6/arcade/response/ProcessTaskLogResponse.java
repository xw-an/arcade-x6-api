package com.x6.arcade.response;

import com.x6.arcade.entity.ProcessInstanceLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProcessTaskLogResponse implements Serializable {

    private Long taskId;

    private int status;

    private Date createdTime;

    private String createdBy;

    private List<ProcessInstanceLog> processInstanceLogs;

}