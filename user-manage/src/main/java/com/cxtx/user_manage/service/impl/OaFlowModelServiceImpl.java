package com.cxtx.user_manage.service.impl;

import com.cxtx.common.unit.ServiceUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaFlowModel;
import com.cxtx.user_manage.mapper.OaFlowModelMapper;
import com.cxtx.user_manage.service.OaFlowModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Map;


@Service
@Transactional(rollbackFor = Exception.class)
public class OaFlowModelServiceImpl implements OaFlowModelService {


    @Autowired
    private OaFlowModelMapper oaFlowModelMapper;


    @Override
    public int deleteByPrimaryKey(Long id) {
        return oaFlowModelMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(OaFlowModel record) {
        return oaFlowModelMapper.insert(record);
    }

    @Override
    public int insertSelective(OaFlowModel record) {
        return oaFlowModelMapper.insertSelective(record);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public OaFlowModel selectByPrimaryKey(Long id) {
        return oaFlowModelMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(OaFlowModel record) {
        return oaFlowModelMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(OaFlowModel record) {
        return oaFlowModelMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<OaFlowModel> selectAll(Map params) {
        return oaFlowModelMapper.selectAll(params);
    }

    @Override
    public PageInfo<OaFlowModel> queryByPage(Map params) {
        ServiceUtil.checkPageParams(params);
        PageHelper.startPage(params);
        List<OaFlowModel> labels = oaFlowModelMapper.selectAll(params);
        return new PageInfo<OaFlowModel>(labels);
    }

    @Override
    public OaFlowMod selectByFlowId(Long flowId) {
        // TODO Auto-generated method stub
        return oaFlowModDao.selectByFlowId(flowId);
    }
}