package com.cxtx.user_manage.service;

import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaFormMain;
import java.util.List;
import java.util.Map;



public interface OaFormMainService{

    int deleteByPrimaryKey(Long id) throws Exception;

    int insertSelective(OaFormMain record) throws Exception;

    OaFormMain selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaFormMain record) throws Exception;

    PageInfo<OaFormMain> queryByPage(Map params);

    List<OaFormMain> selectAll();

    List<OaFormMain> list();
}