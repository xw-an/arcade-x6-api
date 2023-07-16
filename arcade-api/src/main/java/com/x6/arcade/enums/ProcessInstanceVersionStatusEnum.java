package com.x6.arcade.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProcessInstanceVersionStatusEnum {

    // 已发布
    PUBLISHED( 1,"已发布"),
    // 未发布
    UNPUBLISHED( 0,"未发布"),
    // 已下线
    OFFLINE(2,"已下线");

    private Integer statusValue;
    private String statusName;

}
