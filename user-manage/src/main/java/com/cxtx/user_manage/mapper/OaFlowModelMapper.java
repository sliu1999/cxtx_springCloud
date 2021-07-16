package com.cxtx.user_manage.mapper;

import com.cxtx.user_manage.domain.OaFlowModel;

import java.util.List;
import java.util.Map;

public interface OaFlowModelMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OaFlowModel record);

    int insertSelective(OaFlowModel record);

    OaFlowModel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaFlowModel record);

    int updateByPrimaryKey(OaFlowModel record);

    List<OaFlowModel> selectAll(Map params);

    OaFlowModel selectByFlowId(Long flowId);

}