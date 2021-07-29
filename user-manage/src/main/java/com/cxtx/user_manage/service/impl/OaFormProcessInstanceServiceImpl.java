package com.cxtx.user_manage.service.impl;

import com.cxtx.common.unit.ServiceUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaFormProcessInstance;
import com.cxtx.user_manage.mapper.OaFormProcessInstanceMapper;
import com.cxtx.user_manage.service.OaFormProcessInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
@Transactional(rollbackFor = Exception.class)
public class OaFormProcessInstanceServiceImpl implements OaFormProcessInstanceService {


    @Autowired
    private OaFormProcessInstanceMapper oaFormProcessInstanceMapper;


    @Override
    public int deleteByPrimaryKey(Long id) {
        return oaFormProcessInstanceMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(OaFormProcessInstance record) {
        return oaFormProcessInstanceMapper.insertSelective(record);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public OaFormProcessInstance selectByPrimaryKey(Long id) {
        return oaFormProcessInstanceMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(OaFormProcessInstance record) {
        return oaFormProcessInstanceMapper.updateByPrimaryKeySelective(record);
    }


    @Override
    public List<OaFormProcessInstance> selectAll() {
        return oaFormProcessInstanceMapper.selectAll();
    }

    @Override
    public PageInfo<OaFormProcessInstance> queryByPage(Map params) {
        ServiceUtil.checkPageParams(params);
        PageHelper.startPage(params);
        List<OaFormProcessInstance> labels = oaFormProcessInstanceMapper.selectAll();
        return new PageInfo<OaFormProcessInstance>(labels);
    }
}