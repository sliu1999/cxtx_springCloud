package com.cxtx.user_manage.service.impl;

import com.cxtx.common.unit.ServiceUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaProcess;
import com.cxtx.user_manage.mapper.OaProcessMapper;
import com.cxtx.user_manage.service.OaProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class OaProcessServiceImpl implements OaProcessService {


    @Autowired
    private OaProcessMapper oaProcessMapper;


    @Override
    public int deleteByPrimaryKey(Long id) {
        return oaProcessMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(OaProcess record) {
        return oaProcessMapper.insertSelective(record);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public OaProcess selectByPrimaryKey(Long id) {
        return oaProcessMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(OaProcess record) {
        return oaProcessMapper.updateByPrimaryKeySelective(record);
    }
    @Override
    public List<OaProcess> selectAll(Map params) {
        return oaProcessMapper.selectAll(params);
    }


    @Override
    public PageInfo<OaProcess> queryByPage(Map params) {
        ServiceUtil.checkPageParams(params);
        PageHelper.startPage(params);
        List<OaProcess> labels = oaProcessMapper.selectAll(params);
        return new PageInfo<OaProcess>(labels);
    }
}