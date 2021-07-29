package com.cxtx.user_manage.mapper;

import com.cxtx.user_manage.domain.OaFormMain;

import java.util.List;

public interface OaFormMainMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(OaFormMain record);

    OaFormMain selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaFormMain record);

    List<OaFormMain> selectAll();
}