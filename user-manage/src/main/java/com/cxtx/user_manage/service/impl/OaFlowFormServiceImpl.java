package com.cxtx.user_manage.service.impl;

import com.cxtx.common.unit.ServiceUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaFlowForm;
import com.cxtx.user_manage.mapper.OaFlowFormMapper;
import com.cxtx.user_manage.service.OaFlowFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
@Transactional(rollbackFor = Exception.class)
public class OaFlowFormServiceImpl implements OaFlowFormService {


    @Autowired
    private OaFlowFormMapper oaFlowFormMapper;


    @Override
    public int deleteByPrimaryKey(Long id) {
        return oaFlowFormMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(OaFlowForm record) {
        return oaFlowFormMapper.insertSelective(record);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public OaFlowForm selectByPrimaryKey(Long id) {
        return oaFlowFormMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(OaFlowForm record) {
        return oaFlowFormMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<OaFlowForm> selectAll(Map params) {
        return oaFlowFormMapper.selectAll(params);
    }

    @Override
    public OaFlowForm selectByFlowModelId(Long flowModelId) {
        return oaFlowFormMapper.selectByFlowModelId(flowModelId);
    }

    @Override
    public PageInfo<OaFlowForm> queryByPage(Map params) {
        ServiceUtil.checkPageParams(params);
        PageHelper.startPage(params);
        List<OaFlowForm> labels = oaFlowFormMapper.selectAll(params);
        return new PageInfo<OaFlowForm>(labels);
    }


}