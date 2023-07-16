package com.x6.arcade.service;

import com.x6.arcade.entity.ProcessTask;
import com.x6.arcade.response.AllComponentsResponse;

import java.util.List;

public interface ICommonService {

    // 查询所有分类组件
    public List<AllComponentsResponse> listTools();

    public void writeLogMessage(String message, ProcessTask task);

    public void writeLog(Long processNodeId,String message, String processType,String processParameter,String processResult,ProcessTask task);
}
