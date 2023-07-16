package com.x6.arcade.request;

import com.x6.arcade.entity.ProcessInstanceParameter;
import com.x6.arcade.entity.ProcessNode;
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
public class ProcessNodeDetailRequest implements Serializable {

    private ProcessNode processNode;

    private List<ProcessInstanceParameter> processInstanceParameter;

}
