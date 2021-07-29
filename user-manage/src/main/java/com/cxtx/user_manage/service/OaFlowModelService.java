package com.cxtx.user_manage.service;

import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaFlowModel;
import java.util.List;
import java.util.Map;



public interface OaFlowModelService{

    int deleteByPrimaryKey(Long id);

    int insertSelective(OaFlowModel record);

    OaFlowModel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaFlowModel record);

    PageInfo<OaFlowModel> queryByPage(Map params);

    List<OaFlowModel> selectAll(Map params);

    OaFlowModel selectByFlowId(Long flowId);
}