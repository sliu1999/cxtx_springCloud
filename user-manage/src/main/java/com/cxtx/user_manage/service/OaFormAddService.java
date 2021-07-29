package com.cxtx.user_manage.service;

import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaFormAdd;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OaFormAddService{

    int deleteByPrimaryKey(Long id);

    int insertSelective(OaFormAdd record);

    OaFormAdd selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaFormAdd record);

    PageInfo<OaFormAdd> queryByPage(Map params);

    List<OaFormAdd> selectAll();

    List<Map<String, Object>> getFormIdByAuth(Map<String, Object> map);
}