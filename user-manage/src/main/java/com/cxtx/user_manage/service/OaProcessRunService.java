package com.cxtx.user_manage.service;

import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaProcessRun;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;



public interface OaProcessRunService{

    int deleteByPrimaryKey(Long id);


    int insertSelective(OaProcessRun record);

    OaProcessRun selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaProcessRun record);


    PageInfo<Map<String,Object>> queryByPage(Map params);

    List<Map<String,Object>> selectAll(Map params);

    List<OaProcessRun> selectList(Map params);

    int deleteByMap(Map param);

    int deleteByProcessId(Long processId);


}