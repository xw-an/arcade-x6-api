package com.x6.arcade.response;

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
public class ProcessLinkDetailResponse  implements Serializable {

    private Long versionId;

    private Long processInstanceId;

    private Long linkId;

    private ProcessLink processLink;

    private String sourceUuid;

    private String targetUuid;

    private List<ProcessInstanceParameter> processInstanceParameters;

}
