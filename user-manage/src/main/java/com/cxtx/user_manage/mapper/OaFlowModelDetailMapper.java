package com.cxtx.user_manage.mapper;

import com.cxtx.user_manage.domain.OaFlowModelDetail;

import java.util.List;
import java.util.Map;

public interface OaFlowModelDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OaFlowModelDetail record);

    int insertSelective(OaFlowModelDetail record);

    OaFlowModelDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaFlowModelDetail record);

    int updateByPrimaryKey(OaFlowModelDetail record);

    List<OaFlowModelDetail> selectAll(Map params);
}