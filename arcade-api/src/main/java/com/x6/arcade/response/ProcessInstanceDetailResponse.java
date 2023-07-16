package com.x6.arcade.response;

import com.x6.arcade.entity.*;
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
public class ProcessInstanceDetailResponse implements Serializable {

    // 显示的当前版本
    private Long versionId;

    private Long processInstanceId;

    // 生效的实例对象
    private ProcessInstance processInstance;

    // 该流程实例下的所有版本
    private List<ProcessInstanceVersion> processInstanceVersions;

    // 该实例对应的节点信息
    private List<ProcessNodeDetailResponse> processNodeResponse;

    // 该实例对应的连线信息
    private List<ProcessLinkDetailResponse> processLinkResponse;

//    // 该实例对应的参数信息
//    private List<ProcessInstanceParameter> processInstanceParameters;

}
