package com.cxtx.user_manage.service;

import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaProcessRun;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;



public interface OaProcessRunService{

    int deleteByPrimaryKey(Long id);

    int insert(OaProcessRun record);

    int insertSelective(OaProcessRun record);

    OaProcessRun selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaProcessRun record);

    int updateByPrimaryKey(OaProcessRun record);

    PageInfo<OaProcessRun> queryByPage(Map params);

    List<OaProcessRun> selectAll(Map params);

    /**
     * 根据用户ID获取待办流程
     * @param userId
     * @return
     */
    List<OaProcessRun> getMyProcess(Long userId);

    @SuppressWarnings("rawtypes")
    Page getRunProcess(@Param("map")Map<String,Object> map, int pageNum, int pageSize);

    @SuppressWarnings("rawtypes")
    IPage getRunProcess(Page page, Map<String, Object> data);
}