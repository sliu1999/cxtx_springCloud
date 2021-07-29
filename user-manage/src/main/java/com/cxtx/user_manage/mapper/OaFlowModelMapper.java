package com.cxtx.user_manage.mapper;

import com.cxtx.user_manage.domain.OaFlowModel;
import com.cxtx.user_manage.domain.OaFormModel;
import sun.util.calendar.LocalGregorianCalendar;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OaFlowModelMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(OaFlowModel record);

    OaFlowModel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaFlowModel record);

    List<OaFlowModel> selectAll(Map params);

    OaFlowModel selectByFlowId(Long flowId);

    OaFlowModel selectByCreateDate(Date createDate);

    int deleteByFlowId(Long flowId);

}