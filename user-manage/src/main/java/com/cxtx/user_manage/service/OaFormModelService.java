package com.cxtx.user_manage.service;

import com.cxtx.common.domain.JwtModel;
import com.cxtx.user_manage.domain.OaProcessHis;
import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaFormModel;
import java.util.List;
import java.util.Map;


public interface OaFormModelService{

    int deleteByPrimaryKey(Long id);

    int insertSelective(OaFormModel record);

    OaFormModel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaFormModel record) throws Exception;

    PageInfo<OaFormModel> queryByPage(Map params);

    List<OaFormModel> selectAll(Map params);

    /**
     * 根据表单模型ID查询发起节点的表单配置
     * @param formModId
     * @return
     */
    Map<String, Object> getStartElementConfigByFormModId(Long formModId);
    /**
     * 插入表单数据并返回表单ID
     * @param data
     * @param user
     * @return
     * @throws Exception
     */
    Long insertFormData(Map<String,Object> data, JwtModel user)throws Exception;

    int updateFormData(Map<String,Object> data,JwtModel user)throws Exception;

    Map getFormListByAuth(String userId);


}