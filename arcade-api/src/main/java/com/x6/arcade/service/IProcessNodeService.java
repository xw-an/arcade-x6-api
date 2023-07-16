package com.x6.arcade.service;

import com.x6.arcade.entity.ProcessInstanceParameter;
import com.x6.arcade.entity.ProcessNode;
import com.x6.arcade.response.ProcessNodeDetailResponse;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface IProcessNodeService {

    // 创建流程实例的node节点
    public Long createNode(ProcessNode processNode, List<ProcessInstanceParameter> processInstanceParameters);

    // 删除流程实例的node节点
    public void deleteNode(ProcessNode processNode);

    // 更新流程实例的node节点信息
    public void updateNode(ProcessNode processNode,List<ProcessInstanceParameter> processInstanceParameters);

    // 查询流程实例的node节点信息以及节点参数
    public List<ProcessNodeDetailResponse> getNodes(Long versionId,Long processInstanceId);

    // 更新流程实例的node节点x,y坐标
    public void updateNodeCoordinate(ProcessNode processNode);

    // 更新流程实例的node节点大小：宽，长
    public void updateNodeSize(ProcessNode processNode);
}
