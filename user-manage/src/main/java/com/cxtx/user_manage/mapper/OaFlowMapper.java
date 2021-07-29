package com.cxtx.user_manage.mapper;

import com.cxtx.user_manage.domain.OaFlow;

import java.util.List;
import java.util.Map;

public interface OaFlowMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(OaFlow record);

    OaFlow selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaFlow record);

    List<OaFlow> selectAll(Map params);

    OaFlow selectOne(Map params);

    OaFlow selectFlowByForm(Long formId);
}