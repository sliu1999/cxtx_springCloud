package com.cxtx.user_manage.mapper;

import com.cxtx.user_manage.domain.OaProcessRun;

import java.util.List;
import java.util.Map;

public interface OaProcessRunMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(OaProcessRun record);

    OaProcessRun selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaProcessRun record);

    List<Map<String,Object>> selectAll(Map params);

    List<OaProcessRun> selectList(Map params);

    int deleteByMap(Map param);

    int deleteByProcessId(Long processId);

}