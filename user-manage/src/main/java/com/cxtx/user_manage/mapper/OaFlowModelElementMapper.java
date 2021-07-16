package com.cxtx.user_manage.mapper;

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

    int updateByPrimaryKey(OaFlowModelElement record);

    List<OaFlowModelElement> selectAll(Map params);

    Map<String, Object> getNowNodeConfig(@Param("modId")Long modId, @Param("code")String code);

    List<OaFlowModDetail> getLastNextNodes(@Param("modId")Long modId,@Param("code")String code);

    List<Map<String,Object>> getNodeByProcessId(@Param("processId")Long processId);
}