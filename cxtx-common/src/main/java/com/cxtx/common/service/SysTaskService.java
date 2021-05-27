package com.cxtx.common.service;

import com.cxtx.common.domain.SysTask;

import java.util.HashMap;
import java.util.List;

public interface SysTaskService {
    List<SysTask> selectAllAbledTask();

    Boolean isEnabled();

    public List<HashMap> queryAllSysParameter();
}
