package com.cxtx.user_manage.mapper;

import com.cxtx.user_manage.domain.OaFormFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OaFormFileMapper {
    int deleteByPrimaryKey(Long id);

    int insertSelective(OaFormFile record);

    OaFormFile selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaFormFile record);


    List<OaFormFile> selectAll();

    List<OaFormFile> getListByIds(@Param("ids") List<Long> ids);
}