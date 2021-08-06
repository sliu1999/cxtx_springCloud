package com.cxtx.user_manage.service;

import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaFormFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;



public interface OaFormFileService{

    int deleteByPrimaryKey(Long id);

    OaFormFile uploadImg(MultipartFile file) throws IOException;

    OaFormFile selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaFormFile record);

    PageInfo<OaFormFile> queryByPage(Map params);

    List<OaFormFile> selectAll();

    List<OaFormFile> getListByIds(List<Long> ids);
}