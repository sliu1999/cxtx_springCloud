package com.cxtx.common.service.impl;

import com.cxtx.common.domain.SysRestLog;
import com.cxtx.common.mapper.SysRestLogMapper;
import com.cxtx.common.service.SysRestLogService;
import com.cxtx.common.unit.ServiceUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(
        rollbackFor = {Exception.class}
)
public class SysRestLogServiceImpl implements SysRestLogService {
    @Autowired
    private SysRestLogMapper sysRestLogMapper;


    @Override
    public int insert(SysRestLog record) {
        return this.sysRestLogMapper.insert(record);
    }

    @Override
    public List<SysRestLog> selectAll(Map params) {
        return this.sysRestLogMapper.selectByCondition(params);
    }

    @Override
    public PageInfo<SysRestLog> queryByPage(Map params) {
        ServiceUtil.checkPageParams(params);
        PageHelper.startPage(params);
        List<SysRestLog> labels = this.sysRestLogMapper.selectByCondition(params);
        return new PageInfo(labels);
    }
}