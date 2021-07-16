package com.cxtx.user_manage.service;

import com.cxtx.user_manage.domain.OaFormModel;
import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaFlowModelElementConfig;
import java.util.List;
import java.util.Map;



public interface OaFlowModelElementConfigService{

    int deleteByPrimaryKey(Long id);

    OaFlowModelElementConfig selectByPrimaryKey(Long id);

    int insertSelective(OaFlowModelElementConfig record);

    int updateByPrimaryKeySelective(OaFlowModelElementConfig record);

    PageInfo<OaFlowModelElementConfig> queryByPage(Map params);

    List<OaFlowModelElementConfig> selectAll(Map params);
}