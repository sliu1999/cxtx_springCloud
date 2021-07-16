package com.cxtx.user_manage.service;

import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaFlow;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * OaFlow业务接口
 * <p>
 * generated by Handsmap New Technology R&D Center  on 2021-7-16 9:22:26
 * create by sliu
 */

public interface OaFlowService{

    int deleteByPrimaryKey(Long id);

    int insertSelective(OaFlow record);

    OaFlow selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaFlow record);

    PageInfo<OaFlow> queryByPage(Map params);

    List<OaFlow> selectAll(Map params);

    boolean saveFlow(OaFlow oaFlow);

    @SuppressWarnings("rawtypes")
    List getFlowTypeByModName(String modType);

    boolean editFlow(@Valid OaFlow oaFlow);

    //Result<?> addFlow(@Valid OaFlowDTO oaFlowDTO) throws Exception;
    /**
     * 根据流程ID查询流程详情
     * @param flowId
     * @return
     */
    OaFlowDTO findFlow(Long flowId);
    OaFlowDTO newFindFlow  (Long flowId, Date processDate);

    /**
     * 流程的新增和编辑
     * @param oaFlowDTO
     * @return
     * @throws Exception
     */

    Result<?> addOrEditFlow(@Valid OaFlowDTO oaFlowDTO)throws Exception;

    /**
     * 根据流程ID删除流程
     * @param flowId
     * @return
     * @throws Exception
     */
    Result<?> deleteFlow(Long flowId) throws Exception;

    OaFlow selectFlowByForm(Long formId);
}