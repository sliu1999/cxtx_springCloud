package com.cxtx.user_manage.mapper;

import com.cxtx.user_manage.domain.OaFlowModel;
import com.cxtx.user_manage.domain.OaProcessHis;

import java.util.List;
import java.util.Map;

public interface OaProcessHisMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(OaProcessHis record);

    OaProcessHis selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaProcessHis record);;

    List<OaProcessHis> selectAll(Map params);

    List<Map<String,Object>> queryByPageMap(Map params);
}