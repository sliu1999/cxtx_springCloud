package com.cxtx.user_manage.service;

import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaProcessHis;
import java.util.List;
import java.util.Map;



public interface OaProcessHisService{

    int deleteByPrimaryKey(Long id);

    int insert(OaProcessHis record);

    int insertSelective(OaProcessHis record);

    OaProcessHis selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaProcessHis record);

    int updateByPrimaryKey(OaProcessHis record);

    PageInfo<OaProcessHis> queryByPage(Map params);

    List<OaProcessHis> selectAll(Map params);


    /**
     * 根据用户ID获取历史流程
     * @param userId
     * @return
     */
    List<OaProcessHis> getMyHisProcess(Long userId);

    @SuppressWarnings("rawtypes")
    IPage getHisProcessByPage(Page page,Map<String,Object> map);

    @SuppressWarnings("rawtypes")
    Page getHisProcess(Map<String,Object> map,int pageNum,int pageSize);

    List<Map<String,Object>>newgetHisProcess(Map<String,Object> map);

    @SuppressWarnings("rawtypes")
    IPage getHisProcessByAuth(Long curUserId,int pageSize,int pageNum, Map<String, Object> map);
}