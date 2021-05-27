package com.cxtx.sce_manage.service;

import com.github.pagehelper.PageInfo;
import com.cxtx.sce_manage.domain.SysRestLog;
import java.util.List;
import java.util.Map;



public interface SysRestLogService{

    int deleteByPrimaryKey(String id);

    int insert(SysRestLog record);

    int insertSelective(SysRestLog record);

    SysRestLog selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SysRestLog record);

    int updateByPrimaryKey(SysRestLog record);

    PageInfo<SysRestLog> queryByPage(Map params);

    List<SysRestLog> selectAll();
}