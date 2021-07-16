package com.cxtx.user_manage.service.impl;

import com.cxtx.common.unit.ServiceUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaFlowModelElementConfig;
import com.cxtx.user_manage.mapper.OaFlowModelElementConfigMapper;
import com.cxtx.user_manage.service.OaFlowModelElementConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
@Transactional(rollbackFor = Exception.class)
public class OaFlowModelElementConfigServiceImpl implements OaFlowModelElementConfigService {


    @Autowired
    private OaFlowModelElementConfigMapper oaFlowModelElementConfigMapper;


    @Override
    public int deleteByPrimaryKey(Long id) {
        return oaFlowModelElementConfigMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public OaFlowModelElementConfig selectByPrimaryKey(Long id) {
        return oaFlowModelElementConfigMapper.selectByPrimaryKey(id);
    }

    @Override
    public int insertSelective(OaFlowModelElementConfig record) {
        return oaFlowModelElementConfigMapper.insertSelective(record);
    }


    @Override
    public int updateByPrimaryKeySelective(OaFlowModelElementConfig record) {
        return oaFlowModelElementConfigMapper.updateByPrimaryKeySelective(record);
    }


    @Override
    public List<OaFlowModelElementConfig> selectAll(Map params) {
        return oaFlowModelElementConfigMapper.selectAll(params);
    }

    @Override
    public PageInfo<OaFlowModelElementConfig> queryByPage(Map params) {
        ServiceUtil.checkPageParams(params);
        PageHelper.startPage(params);
        List<OaFlowModelElementConfig> labels = oaFlowModelElementConfigMapper.selectAll(params);
        return new PageInfo<OaFlowModelElementConfig>(labels);
    }
}