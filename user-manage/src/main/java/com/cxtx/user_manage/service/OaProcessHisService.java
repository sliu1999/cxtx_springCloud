package com.cxtx.user_manage.service;

import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaProcessHis;
import java.util.List;
import java.util.Map;



public interface OaProcessHisService{

    int deleteByPrimaryKey(Long id);

    int insertSelective(OaProcessHis record);

    OaProcessHis selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaProcessHis record);

    PageInfo<OaProcessHis> queryByPage(Map params);

    List<OaProcessHis> selectAll(Map params);

    PageInfo<Map<String,Object>> queryByPageMap(Map params);

    int deleteByProcessId(Long processId);

}