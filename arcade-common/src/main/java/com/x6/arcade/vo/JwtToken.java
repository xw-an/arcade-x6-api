package com.x6.arcade.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>授权中心鉴权之后给客户端的 Token</h1>
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtToken {

    /** JWT */
    private String token;
}
