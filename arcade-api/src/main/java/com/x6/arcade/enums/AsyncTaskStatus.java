package com.x6.arcade.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <h1>异步任务状态枚举</h1>
 * */
@Getter
@AllArgsConstructor
public enum AsyncTaskStatus {

    STARTED(0, "未开始"),
    RUNNING(1, "运行中"),
    SUCCESS(2, "执行成功"),
    FAILED(3, "执行失败"),
    ;

    /** 执行状态编码 */
    private int status;

    /** 执行状态描述 */
    private String description;

    // 获取执行状态对应的描述信息
    public static String getAsyncTaskStatusDescription(int status) {
        for (AsyncTaskStatus asyncTaskStatus : AsyncTaskStatus.values()) {
            if (asyncTaskStatus.getStatus() == status) {
                return asyncTaskStatus.getDescription();
            }
        }
        return null;
    }
}
