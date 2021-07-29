package com.cxtx.user_manage.mapper;

import com.cxtx.user_manage.domain.OaFormAdd;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OaFormAddMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(OaFormAdd record);

    OaFormAdd selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaFormAdd record);

    List<OaFormAdd> selectAll();

    int deleteByMainId(Long mainId);

    List<Map<String, Object>> getFormIdByAuth(Map<String, Object> map);
}