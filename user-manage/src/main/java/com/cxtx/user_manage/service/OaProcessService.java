package com.cxtx.user_manage.service;

import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaProcess;
import java.util.List;
import java.util.Map;


public interface OaProcessService{

    int deleteByPrimaryKey(Long id);

    int insertSelective(OaProcess record);

    OaProcess selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaProcess record);

    PageInfo<OaProcess> queryByPage(Map params);

    List<OaProcess> selectAll(Map params);
}