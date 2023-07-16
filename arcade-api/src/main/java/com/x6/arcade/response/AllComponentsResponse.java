package com.x6.arcade.response;

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
public class AllComponentsResponse implements Serializable {

    private String category;

    private List<Children> children;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Children implements Serializable {
        private String businessName;
        private Object children;
    }
}
