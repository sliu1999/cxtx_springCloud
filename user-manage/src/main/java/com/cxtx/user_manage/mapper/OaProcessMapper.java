package com.cxtx.user_manage.mapper;

import com.cxtx.user_manage.domain.OaProcess;

import java.util.List;
import java.util.Map;

public interface OaProcessMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(OaProcess record);

    int updateByPrimaryKeySelective(OaProcess record);

    List<OaProcess> selectAll(Map params);

    OaProcess selectByPrimaryKey(Long id);
}