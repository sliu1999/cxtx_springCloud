package com.cxtx.user_manage.service;

import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaFormProcessInstance;
import java.util.List;
import java.util.Map;



public interface OaFormProcessInstanceService{

    int deleteByPrimaryKey(Long id);

    int insertSelective(OaFormProcessInstance record);

    OaFormProcessInstance selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaFormProcessInstance record);

    PageInfo<OaFormProcessInstance> queryByPage(Map params);

    List<OaFormProcessInstance> selectAll();
}