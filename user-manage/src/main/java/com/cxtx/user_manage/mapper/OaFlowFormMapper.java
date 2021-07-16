package com.cxtx.user_manage.mapper;

import com.cxtx.user_manage.domain.OaFlowForm;

import java.util.List;
import java.util.Map;

public interface OaFlowFormMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OaFlowForm record);

    int insertSelective(OaFlowForm record);

    OaFlowForm selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaFlowForm record);

    int updateByPrimaryKey(OaFlowForm record);

    List<OaFlowForm> selectAll(Map params);
}