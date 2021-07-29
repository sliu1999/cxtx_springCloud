package com.cxtx.user_manage.service.impl;

import com.cxtx.common.domain.JwtModel;
import com.cxtx.common.unit.HttpServletUtils;
import com.cxtx.common.unit.ServiceUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaProcessHis;
import com.cxtx.user_manage.mapper.OaProcessHisMapper;
import com.cxtx.user_manage.service.OaProcessHisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Transactional(rollbackFor = Exception.class)
public class OaProcessHisServiceImpl implements OaProcessHisService {


    @Autowired
    private OaProcessHisMapper oaProcessHisMapper;


    @Override
    public int deleteByPrimaryKey(Long id) {
        return oaProcessHisMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(OaProcessHis record) {
        return oaProcessHisMapper.insertSelective(record);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public OaProcessHis selectByPrimaryKey(Long id) {
        return oaProcessHisMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(OaProcessHis record) {
        return oaProcessHisMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<OaProcessHis> selectAll(Map params) {
        return oaProcessHisMapper.selectAll(params);
    }

    @Override
    public PageInfo<Map<String, Object>> queryByPageMap(Map params) {
        JwtModel curUser = HttpServletUtils.getUserInfo();
        params.put("userId",curUser.getUserId());
        ServiceUtil.checkPageParams(params);
        PageHelper.startPage(params);
        List<Map<String, Object>> labels = oaProcessHisMapper.queryByPageMap(params);
        return new PageInfo<Map<String, Object>>(labels);
    }

    @Override
    public PageInfo<OaProcessHis> queryByPage(Map params) {
        ServiceUtil.checkPageParams(params);
        PageHelper.startPage(params);
        List<OaProcessHis> labels = oaProcessHisMapper.selectAll(params);
        return new PageInfo<OaProcessHis>(labels);
    }

}