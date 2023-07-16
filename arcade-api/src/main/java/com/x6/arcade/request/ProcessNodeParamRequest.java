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
public class ProcessNodeParamRequest implements Serializable {

    private Long versionId;

    private Long processInstanceId;

    private String processNodeUuid;

    private String parameterType;

}
