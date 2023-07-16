package com.x6.arcade.response;

import com.x6.arcade.entity.ProcessInstanceParameter;
import com.x6.arcade.entity.ProcessNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProcessNodeParamResponse implements Serializable {

    private String type;

    private ProcessNode processNode;

    private ProcessInstanceParameter processInstanceParameter;

}
