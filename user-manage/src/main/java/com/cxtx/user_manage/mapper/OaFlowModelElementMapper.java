package com.cxtx.user_manage.mapper;

import com.cxtx.user_manage.domain.OaFlowModelDetail;
import com.cxtx.user_manage.domain.OaFlowModelElement;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OaFlowModelElementMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OaFlowModelElement record);

    int insertSelective(OaFlowModelElement record);

    OaFlowModelElement selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaFlowModelElement record);

    List<OaFlowModelElement> selectAll(Map params);

    Map<String, Object> getNowNodeConfig(@Param("flowModelId")Long flowModelId, @Param("code")String code);

    List<OaFlowModelDetail> getLastNextNodes(@Param("modId")Long modId, @Param("code")String code);

    List<Map<String,Object>> getNodeByProcessId(@Param("processId")Long processId);

    int deleteByModelId(Long modelId);

    OaFlowModelElement selectOneByCodeAndModelId(@Param("code") String code, @Param("modelId") Long modelId);

}