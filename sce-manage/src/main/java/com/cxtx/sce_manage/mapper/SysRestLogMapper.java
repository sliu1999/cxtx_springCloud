package com.cxtx.sce_manage.mapper;

import com.cxtx.sce_manage.domain.SysRestLog;

import java.util.List;

public interface SysRestLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRestLog record);

    int insertSelective(SysRestLog record);

    SysRestLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRestLog record);

    int updateByPrimaryKey(SysRestLog record);

    List<SysRestLog> selectAll();


}