package com.cxtx.user_manage.mapper;

import com.cxtx.user_manage.domain.OaFormProcessInstance;

import java.util.List;

public interface OaFormProcessInstanceMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(OaFormProcessInstance record);

    OaFormProcessInstance selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaFormProcessInstance record);

    List<OaFormProcessInstance> selectAll();
}