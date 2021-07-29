package com.cxtx.user_manage.service.impl;

import com.cxtx.common.unit.ServiceUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaFormAdd;
import com.cxtx.user_manage.mapper.OaFormAddMapper;
import com.cxtx.user_manage.service.OaFormAddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
@Transactional(rollbackFor = Exception.class)
public class OaFormAddServiceImpl implements OaFormAddService {


    @Autowired
    private OaFormAddMapper oaFormAddMapper;


    @Override
    public int deleteByPrimaryKey(Long id) {
        return oaFormAddMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(OaFormAdd record) {
        return oaFormAddMapper.insertSelective(record);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public OaFormAdd selectByPrimaryKey(Long id) {
        return oaFormAddMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(OaFormAdd record) {
        return oaFormAddMapper.updateByPrimaryKeySelective(record);
    }


    @Override
    public List<OaFormAdd> selectAll() {
        return oaFormAddMapper.selectAll();
    }

    @Override
    public List<Map<String, Object>> getFormIdByAuth(Map<String, Object> map) {
        return oaFormAddMapper.getFormIdByAuth(map);
    }

    @Override
    public PageInfo<OaFormAdd> queryByPage(Map params) {
        ServiceUtil.checkPageParams(params);
        PageHelper.startPage(params);
        List<OaFormAdd> labels = oaFormAddMapper.selectAll();
        return new PageInfo<OaFormAdd>(labels);
    }
}