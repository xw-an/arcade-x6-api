package com.x6.arcade.service;


import com.x6.arcade.entity.ProcessNode;
import com.x6.arcade.entity.ProcessTask;

/**
 * <h1>异步服务接口定义</h1>
 * */
public interface IAsyncService {

    /**
     * <h2>异步执行流程实例</h2>
     * */
    void asyncExecuteInstance(ProcessTask task, ProcessNode firstNode, ProcessNode lastNode);

}
