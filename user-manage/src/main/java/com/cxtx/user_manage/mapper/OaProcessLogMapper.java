package com.cxtx.user_manage.mapper;

import com.cxtx.user_manage.domain.OaProcessLog;

import java.util.List;
import java.util.Map;

public interface OaProcessLogMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(OaProcessLog record);


    int updateByPrimaryKeySelective(OaProcessLog record);

    List<OaProcessLog> selectAll(Map params);

    OaProcessLog selectByPrimaryKey(Long id);

    List<OaProcessLog> getLogByProcessId(Long processId);
}