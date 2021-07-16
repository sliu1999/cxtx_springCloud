package com.cxtx.user_manage.mapper;

import com.cxtx.user_manage.domain.OaFlowModelElementConfig;

import java.util.List;
import java.util.Map;

public interface OaFlowModelElementConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(OaFlowModelElementConfig record);

    int updateByPrimaryKeySelective(OaFlowModelElementConfig record);

    List<OaFlowModelElementConfig> selectAll(Map params);

    OaFlowModelElementConfig selectByPrimaryKey(Long id);
}