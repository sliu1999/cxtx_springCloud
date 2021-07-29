package com.cxtx.user_manage.service;

import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaFlow;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;



public interface OaFlowService{

    int deleteByPrimaryKey(Long id) throws Exception;

    int insertSelective(OaFlow record);

    OaFlow selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaFlow record);

    PageInfo<OaFlow> queryByPage(Map params);

    List<OaFlow> selectAll(Map params);

    OaFlow newFindFlow  (Long flowId, Date processDate);

    Map addOrEditFlow(OaFlow oaFlow)throws Exception;

    OaFlow selectFlowByForm(Long formId);
}