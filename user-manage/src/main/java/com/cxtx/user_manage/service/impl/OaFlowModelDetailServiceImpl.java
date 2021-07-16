package com.cxtx.user_manage.service.impl;

import com.cxtx.common.unit.ServiceUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaFlowModelDetail;
import com.cxtx.user_manage.mapper.OaFlowModelDetailMapper;
import com.cxtx.user_manage.service.OaFlowModelDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
@Transactional(rollbackFor = Exception.class)
public class OaFlowModelDetailServiceImpl implements OaFlowModelDetailService {


    @Autowired
    private OaFlowModelDetailMapper oaFlowModelDetailMapper;


    @Override
    public int deleteByPrimaryKey(Long id) {
        return oaFlowModelDetailMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(OaFlowModelDetail record) {
        return oaFlowModelDetailMapper.insert(record);
    }

    @Override
    public int insertSelective(OaFlowModelDetail record) {
        return oaFlowModelDetailMapper.insertSelective(record);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public OaFlowModelDetail selectByPrimaryKey(Long id) {
        return oaFlowModelDetailMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(OaFlowModelDetail record) {
        return oaFlowModelDetailMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(OaFlowModelDetail record) {
        return oaFlowModelDetailMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<OaFlowModelDetail> selectAll(Map params) {
        return oaFlowModelDetailMapper.selectAll(params);
    }

    @Override
    public PageInfo<OaFlowModelDetail> queryByPage(Map params) {
        ServiceUtil.checkPageParams(params);
        PageHelper.startPage(params);
        List<OaFlowModelDetail> labels = oaFlowModelDetailMapper.selectAll(params);
        return new PageInfo<OaFlowModelDetail>(labels);
    }
}