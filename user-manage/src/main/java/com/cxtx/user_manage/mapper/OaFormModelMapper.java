package com.cxtx.user_manage.mapper;

import com.cxtx.user_manage.domain.OaFormModel;
import com.cxtx.user_manage.domain.OaProcessHis;

import java.util.List;
import java.util.Map;

public interface OaFormModelMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(OaFormModel record);

    int updateByPrimaryKeySelective(OaFormModel record);

    List<OaFormModel> selectAll(Map params);

    OaFormModel selectByPrimaryKey(Long id);
}