package com.cxtx.user_manage.service;

import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaFlowForm;
import java.util.List;
import java.util.Map;


public interface OaFlowFormService{

    int deleteByPrimaryKey(Long id);

    int insert(OaFlowForm record);

    int insertSelective(OaFlowForm record);

    OaFlowForm selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaFlowForm record);

    int updateByPrimaryKey(OaFlowForm record);

    PageInfo<OaFlowForm> queryByPage(Map params);

    List<OaFlowForm> selectAll(Map params);
}