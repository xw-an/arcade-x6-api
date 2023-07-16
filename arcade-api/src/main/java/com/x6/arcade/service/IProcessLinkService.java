package com.x6.arcade.service;

import com.x6.arcade.entity.ProcessLink;
import com.x6.arcade.request.ProcessLinkDetailRequest;
import com.x6.arcade.response.ProcessLinkDetailResponse;

import java.util.List;

public interface IProcessLinkService {

    // 创建流程实例的node节点之间的连线
    public Long createLink(ProcessLinkDetailRequest request);

    // 删除流程实例的node节点之间的连线
    public void deleteLink(ProcessLink request);

    // 删除节点关联的所有联系
    public void deleteLinkByNodeId(ProcessLink request);

    // 更新流程实例的node节点之间的连线
    public void updateLink(ProcessLinkDetailRequest request);

    // 更新连接线目标节点
    public void updateLinkTarget(ProcessLinkDetailRequest request);

    // 查询流程实例的node节点之间的连线信息以及连线参数
    public List<ProcessLinkDetailResponse> getLinks(Long versionId,Long processInstanceId);


}