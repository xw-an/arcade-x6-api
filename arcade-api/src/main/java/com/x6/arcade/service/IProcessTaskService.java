package com.x6.arcade.service;

import com.github.pagehelper.PageInfo;
import com.x6.arcade.entity.ProcessTaskInfo;
import com.x6.arcade.response.ProcessTaskListRequest;

public interface IProcessTaskService {

    // 根据筛选条件查询有效的流程实例
    public PageInfo<ProcessTaskInfo> getTaskList(ProcessTaskListRequest request);
}
