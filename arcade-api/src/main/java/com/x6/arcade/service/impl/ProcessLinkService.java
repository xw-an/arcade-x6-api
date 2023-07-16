package com.x6.arcade.service.impl;

import com.x6.arcade.dao.ProcessInstanceParameterMapper;
import com.x6.arcade.dao.ProcessLinkMapper;
import com.x6.arcade.dao.ProcessNodeMapper;
import com.x6.arcade.entity.ProcessInstanceParameter;
import com.x6.arcade.entity.ProcessLink;
import com.x6.arcade.entity.ProcessNode;
import com.x6.arcade.request.ProcessLinkDetailRequest;
import com.x6.arcade.response.ProcessLinkDetailResponse;
import com.x6.arcade.service.IProcessLinkService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ProcessLinkService implements IProcessLinkService {

    @Autowired
    private ProcessLinkMapper processLinkMapper;
    @Autowired
    private ProcessInstanceParameterMapper processInstanceParameterMapper;
    @Autowired
    private ProcessNodeMapper processNodeMapper;
    @Autowired
    private ProcessInstanceVersionService processInstanceVersionService;

    @Transactional
    @Override
    public Long createLink(ProcessLinkDetailRequest request) {
        // 查询流程实例的版本的状态，如果是已发布/已下线，则不允许创建连线
        if (!processInstanceVersionService.checkProcessInstanceVersionStatus(request.getProcessLink().getVersionId())) throw new RuntimeException("当前流程版本已发布/已下线，不能创建连线");
        ProcessLink link = request.getProcessLink();
        Long sourceId = processNodeMapper.getIdByUuid(ProcessNode.builder()
            .versionId(link.getVersionId())
            .processInstanceId(link.getProcessInstanceId())
            .uuid(request.getSourceNodeId())
            .build());
        link.setCreatedTime(new Date());
        link.setUpdatedTime(new Date());
        link.setSourceNodeId(sourceId);
        if (ObjectUtils.isNotEmpty(request.getTargetNodeId())){
            Long targetId = processNodeMapper.getIdByUuid(ProcessNode.builder()
                .versionId(link.getVersionId())
                .processInstanceId(link.getProcessInstanceId())
                .uuid(request.getTargetNodeId())
                .build());
            link.setTargetNodeId(targetId);
        } else {
            link.setTargetNodeId(0L);
            link.setIsDelete(-1);
        }
        processLinkMapper.insertSelective(link);
        request.getProcessInstanceParameter().stream().forEach(processInstanceParameter -> {
            processInstanceParameter.setVersionId(link.getVersionId());
            processInstanceParameter.setProcessLinkId(link.getId());
            processInstanceParameter.setProcessInstanceId(link.getProcessInstanceId());
            processInstanceParameter.setCreatedTime(new Date());
            processInstanceParameter.setUpdatedTime(new Date());
            processInstanceParameterMapper.insertSelective(processInstanceParameter);
        });
        return link.getId();
    }

    @Transactional
    @Override
    public void deleteLink(ProcessLink request) {
        // 查询流程实例的版本的状态，如果是已发布/已下线，则不允许删除连线
        if (!processInstanceVersionService.checkProcessInstanceVersionStatus(request.getVersionId())) throw new RuntimeException("当前流程版本已发布/已下线，不能删除连线");
        Long linkId = processLinkMapper.getIdByUuid(request);
        request.setUpdatedTime(new Date());
        processLinkMapper.deleteByProcessInstanceIdAndUuid(request);
        processInstanceParameterMapper.deleteByProcessLinkId(ProcessInstanceParameter.builder()
                .versionId(request.getVersionId())
                .processLinkId(linkId)
                .processInstanceId(request.getProcessInstanceId())
                .updatedTime(new Date())
                .updatedBy(request.getUpdatedBy())
                .build());
    }

    @Transactional
    @Override
    public void deleteLinkByNodeId(ProcessLink request) {
        // 查询流程实例的版本的状态，如果是已发布/已下线，则不允许删除连线关联节点
        if (!processInstanceVersionService.checkProcessInstanceVersionStatus(request.getVersionId())) throw new RuntimeException("当前流程版本已发布/已下线，不能删除连线关联节点");
        request.setUpdatedTime(new Date());
        processLinkMapper.deleteBySourceTargetNodeId(request);
        processLinkMapper.selectBySourceTargetNodeId(request).stream().forEach(processLink -> {
            processInstanceParameterMapper.deleteByProcessLinkId(ProcessInstanceParameter.builder()
                .versionId(processLink.getVersionId())
                .processLinkId(processLink.getId())
                .processInstanceId(request.getProcessInstanceId())
                .updatedTime(new Date())
                .updatedBy(request.getUpdatedBy())
                .build());
        });
    }

    @Transactional
    @Override
    public void updateLink(ProcessLinkDetailRequest request) {
        // 查询流程实例的版本的状态，如果是已发布/已下线，则不允许更新连线
        if (!processInstanceVersionService.checkProcessInstanceVersionStatus(request.getProcessLink().getVersionId())) throw new RuntimeException("当前流程版本已发布/已下线，不能更新连线");
        ProcessLink link = request.getProcessLink();
        Long sourceId = processNodeMapper.getIdByUuid(ProcessNode.builder()
                .versionId(link.getVersionId())
                .processInstanceId(link.getProcessInstanceId())
                .uuid(request.getSourceNodeId())
                .build());
        Long targetId = processNodeMapper.getIdByUuid(ProcessNode.builder()
                .versionId(link.getVersionId())
                .processInstanceId(link.getProcessInstanceId())
                .uuid(request.getTargetNodeId())
                .build());
        link.setUpdatedTime(new Date());
        link.setSourceNodeId(sourceId);
        link.setTargetNodeId(targetId);
        Long linkId = processLinkMapper.getIdByUuid(request.getProcessLink());
        link.setId(linkId);
        processLinkMapper.updateByPrimaryKeySelective(link);
        request.getProcessInstanceParameter().stream().forEach(processInstanceParameter -> {
            processInstanceParameter.setVersionId(link.getVersionId());
            processInstanceParameter.setProcessLinkId(linkId);
            processInstanceParameter.setProcessInstanceId(link.getProcessInstanceId());
            processInstanceParameter.setUpdatedTime(new Date());
            processInstanceParameterMapper.updateByParameterType(processInstanceParameter);
        });
    }

    @Transactional
    @Override
    public void updateLinkTarget(ProcessLinkDetailRequest processLink) {
        // 查询流程实例的版本的状态，如果是已发布/已下线，则不允许更新连接线目标节点
        if (!processInstanceVersionService.checkProcessInstanceVersionStatus(processLink.getProcessLink().getVersionId())) throw new RuntimeException("当前流程版本已发布/已下线，不能更新连接线目标节点");
        ProcessLink link = processLink.getProcessLink();
        link.setTargetNodeId(processNodeMapper.getIdByUuid(ProcessNode.builder()
            .versionId(link.getVersionId())
            .processInstanceId(link.getProcessInstanceId())
            .uuid(processLink.getTargetNodeId())
            .build()));
        link.setUpdatedTime(new Date());
        link.setIsDelete(0);
        processLinkMapper.updateLinkTarget(link);
    }

    @Override
    public List<ProcessLinkDetailResponse> getLinks(Long versionId,Long processInstanceId) {
        List<ProcessLinkDetailResponse> result = new ArrayList<>();
        List<ProcessLink> links = processLinkMapper.selectByParams(ProcessLink.builder()
            .versionId(versionId)
            .processInstanceId(processInstanceId)
            .isDelete(0)
            .build());
        links.forEach(link -> {
            List<ProcessInstanceParameter> parameters = processInstanceParameterMapper.selectByParams(ProcessInstanceParameter.builder()
                .versionId(versionId)
                .processInstanceId(processInstanceId)
                .processLinkId(link.getId())
                .isDelete(0)
                .build());
            result.add(ProcessLinkDetailResponse.builder()
                .versionId(versionId)
                .processLink(link)
                .processInstanceParameters(parameters)
                .processInstanceId(processInstanceId)
                .sourceUuid(processNodeMapper.selectByPrimaryKey(link.getSourceNodeId()).getUuid())
                .targetUuid(processNodeMapper.selectByPrimaryKey(link.getTargetNodeId()).getUuid())
                .linkId(link.getId())
                .build());
        });
        return result;
    }
}
