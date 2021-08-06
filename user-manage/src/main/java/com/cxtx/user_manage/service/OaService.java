package com.cxtx.user_manage.service;

import com.cxtx.common.domain.JwtModel;
import com.cxtx.user_manage.domain.OaFlow;
import com.cxtx.user_manage.domain.OaFlowModelElement;
import com.cxtx.user_manage.domain.User;
import com.github.pagehelper.PageInfo;

import javax.script.ScriptException;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;


public interface OaService {

    /**
     * 获取审批人
     * @param data
     * @return
     */
    List<User> getHandler(Map<String,Object> data, Long creatorId);

    Map submit(Map<String, Object> payload, JwtModel curUser) throws Exception;

    /**
     * 获取下一节点信息
     * @param curCode
     * @param modId
     * @param formData
     * @param userId
     * @return
     */
    OaFlowModelElement getNextNodes(String curCode, Long modId, Map<String, Object> formData, Long userId) throws ScriptException;

    Map getProcessDetail(Long processId);

    Map approval(Map<String,Object> data,JwtModel sysUser) throws Exception;

    Map refuse(Map<String,Object> data,JwtModel sysUser) throws Exception;

    int deleteProcess(Long processId) throws Exception;


}