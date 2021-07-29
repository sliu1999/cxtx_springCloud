package com.cxtx.user_manage.service;

import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaFlowModelElement;
import java.util.List;
import java.util.Map;



public interface OaFlowModelElementService{

    int deleteByPrimaryKey(Long id);

    int insert(OaFlowModelElement record);

    int insertSelective(OaFlowModelElement record);

    OaFlowModelElement selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaFlowModelElement record);

    PageInfo<OaFlowModelElement> queryByPage(Map params);

    List<OaFlowModelElement> selectAll(Map params);

    /**
     * 根据当前节点和流程模型ID获取上一节点及配置信息
     * @param curNode
     * @param modId
     * @return
     */
    List<OaFlowModelElement> getLastNodes(String curNode,Long modId);
    /**
     * 根据当前节点和流程模型ID获取上一节点及配置信息
     * @param curNode
     * @param modId
     * @return
     */
    List<OaFlowModelElement> getNextNodes(String curNode,Long modId);

    Map<String, Object> getNowNodeConfig(String curNode,Long modId);

    /**
     * 根据流程实例ID获取当前流程之前的已审批节点
     * @param processId
     * @return
     */
    List<Map<String, Object>> getNodeByProcessId(Long processId);

    OaFlowModelElement selectOneByCodeAndModelId(String code, Long modelId);

}