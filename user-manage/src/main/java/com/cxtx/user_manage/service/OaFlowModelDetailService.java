package com.cxtx.user_manage.service;

import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaFlowModelDetail;
import java.util.List;
import java.util.Map;


public interface OaFlowModelDetailService{

    int deleteByPrimaryKey(Long id);

    int insertSelective(OaFlowModelDetail record);

    OaFlowModelDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaFlowModelDetail record);

    PageInfo<OaFlowModelDetail> queryByPage(Map params);

    List<OaFlowModelDetail> selectAll(Map params);
}