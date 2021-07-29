package com.cxtx.user_manage.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cxtx.common.unit.ServiceUtil;
import com.cxtx.user_manage.domain.OaProcess;
import com.cxtx.user_manage.domain.OaProcessLog;
import com.cxtx.user_manage.domain.User;
import com.cxtx.user_manage.service.OaProcessLogService;
import com.cxtx.user_manage.service.OaProcessService;
import com.cxtx.user_manage.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaProcessRun;
import com.cxtx.user_manage.mapper.OaProcessRunMapper;
import com.cxtx.user_manage.service.OaProcessRunService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Transactional(rollbackFor = Exception.class)
public class OaProcessRunServiceImpl implements OaProcessRunService {


    @Autowired
    private OaProcessRunMapper oaProcessRunMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private OaProcessLogService oaProcessLogService;

    @Autowired
    private OaProcessService oaProcessService;


    @Override
    public int deleteByPrimaryKey(Long id) {
        return oaProcessRunMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(OaProcessRun record) {
        return oaProcessRunMapper.insertSelective(record);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public OaProcessRun selectByPrimaryKey(Long id) {
        return oaProcessRunMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(OaProcessRun record) {
        return oaProcessRunMapper.updateByPrimaryKeySelective(record);
    }


    @Override
    public List<Map<String,Object>> selectAll(Map params) {
        return oaProcessRunMapper.selectAll(params);
    }

    @Override
    public List<OaProcessRun> selectList(Map params) {
        return oaProcessRunMapper.selectList(params);
    }

    @Override
    public int deleteByMap(Map param) {
        return oaProcessRunMapper.deleteByMap(param);
    }

    @Override
    public int deleteByProcessId(Long processId) {
        return oaProcessRunMapper.deleteByProcessId(processId);
    }

    @Override
    public PageInfo<Map<String,Object>> queryByPage(Map params) {
        ServiceUtil.checkPageParams(params);
        PageHelper.startPage(params);
        List<Map<String,Object>> labels = oaProcessRunMapper.selectAll(params);
        for(Map<String,Object> mm:labels) {
            Long processId = Long.parseLong(mm.get("id").toString());
            HashMap param = new HashMap(1);
            param.put("processId",processId);
            OaProcessLog log = oaProcessLogService.selectAll(param).get(0);
            if(userService.selectUserById(log.getCurAssignee())!=null) {
                mm.put("lastHandler",userService.selectUserById(log.getCurAssignee()).getLoginId());
            }
        }
        return new PageInfo<Map<String,Object>>(labels);
    }

}