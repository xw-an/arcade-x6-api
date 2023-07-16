package com.x6.arcade.request;

import com.x6.arcade.entity.ProcessInstanceParameter;
import com.x6.arcade.entity.ProcessLink;
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
public class ProcessLinkDetailRequest implements Serializable {

    private ProcessLink processLink;

    private String sourceNodeId;

    private String targetNodeId;

    private List<ProcessInstanceParameter> processInstanceParameter;
}