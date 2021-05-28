//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cxtx.user_manage.service.impl;

import com.cxtx.user_manage.domain.SysInfo;
import com.cxtx.user_manage.mapper.SysInfoMapper;
import com.cxtx.user_manage.service.SysInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(
        rollbackFor = {Exception.class}
)
public class SysInfoServiceImpl implements SysInfoService {
    @Autowired
    private SysInfoMapper sysInfoMapper;


    @Override
    public int saveSysInfo(SysInfo record) {
        SysInfo sysInfo = this.sysInfoMapper.selectSysInfo();
        if (sysInfo != null && sysInfo.getId() != null) {
            record.setId(sysInfo.getId());
            return this.sysInfoMapper.updateSysInfo(record);
        } else {
            return this.sysInfoMapper.insertSysInfo(record);
        }
    }

    @Transactional(
            propagation = Propagation.NOT_SUPPORTED
    )
    @Override
    public SysInfo selectSysInfo() {
        return this.sysInfoMapper.selectSysInfo();
    }
}
