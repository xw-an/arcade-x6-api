package com.x6.arcade.service;

import com.x6.arcade.entity.ProcessInstanceVersion;

import java.util.List;

public interface IProcessInstanceVersionService {
    // 获取所有的版本数据
    public List<ProcessInstanceVersion> processInstanceVersionList(Long processInstanceId);

    // 更新版本状态
    public void updateVersionStatus(ProcessInstanceVersion processInstanceVersion,Integer status);

    // 复制版本
    public Long copyVersion(ProcessInstanceVersion processInstanceVersion);

    // 查询版本状态
    public boolean checkProcessInstanceVersionStatus(Long versionId);
}
