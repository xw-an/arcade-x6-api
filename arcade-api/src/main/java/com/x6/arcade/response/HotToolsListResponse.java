package com.x6.arcade.response;

import com.x6.arcade.entity.ProcessInstance;
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
public class HotToolsListResponse  implements Serializable {

    private String category;

    private List<ProcessInstance> processInstances;
}
