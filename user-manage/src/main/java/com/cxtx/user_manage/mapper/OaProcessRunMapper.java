package com.cxtx.user_manage.mapper;

import com.cxtx.user_manage.domain.OaProcessRun;

import java.util.List;
import java.util.Map;

public interface OaProcessRunMapper {
    int deleteByPrimaryKey(Long id);

    int insert(OaProcessRun record);

    int insertSelective(OaProcessRun record);

    OaProcessRun selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaProcessRun record);

    int updateByPrimaryKey(OaProcessRun record);

    List<OaProcessRun> selectAll(Map params);

}