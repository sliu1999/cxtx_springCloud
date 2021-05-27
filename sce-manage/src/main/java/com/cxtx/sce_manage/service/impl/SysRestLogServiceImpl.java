package com.cxtx.sce_manage.service.impl;

import com.cxtx.common.unit.ServiceUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cxtx.sce_manage.domain.SysRestLog;
import com.cxtx.sce_manage.mapper.SysRestLogMapper;
import com.cxtx.sce_manage.service.SysRestLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
@Transactional(rollbackFor = Exception.class)
public class SysRestLogServiceImpl implements SysRestLogService {


    @Autowired
    private SysRestLogMapper sysRestLogMapper;


    @Override
    public int deleteByPrimaryKey(String id) {
        return sysRestLogMapper.deleteByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public int insert(SysRestLog record) {
        return sysRestLogMapper.insert(record);
    }

    @Override
    public int insertSelective(SysRestLog record) {
        return sysRestLogMapper.insertSelective(record);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public SysRestLog selectByPrimaryKey(String id) {
        return sysRestLogMapper.selectByPrimaryKey(Integer.parseInt(id));
    }

    @Override
    public int updateByPrimaryKeySelective(SysRestLog record) {
        return sysRestLogMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SysRestLog record) {
        return sysRestLogMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<SysRestLog> selectAll() {
        return sysRestLogMapper.selectAll();
    }

    @Override
    public PageInfo<SysRestLog> queryByPage(Map params) {
        ServiceUtil.checkPageParams(params);
        PageHelper.startPage(params);
        List<SysRestLog> labels = sysRestLogMapper.selectAll();
        return new PageInfo<SysRestLog>(labels);
    }
}