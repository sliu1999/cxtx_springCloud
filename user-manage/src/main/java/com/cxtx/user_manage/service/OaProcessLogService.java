package com.cxtx.user_manage.service;

import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaProcessLog;
import java.util.List;
import java.util.Map;



public interface OaProcessLogService{

    int deleteByPrimaryKey(Long id);

    int insertSelective(OaProcessLog record);

    int updateByPrimaryKeySelective(OaProcessLog record);

    OaProcessLog selectByPrimaryKey(Long id);

    PageInfo<OaProcessLog> queryByPage(Map params);

    List<OaProcessLog> selectAll(Map params);

    List<OaProcessLog> getLogByProcessId(Long processId);

    int deleteByProcessId(Long processId);
}