package com.cxtx.common.mapper;

import com.cxtx.common.domain.SysTask;

import java.util.HashMap;
import java.util.List;

public interface SysTaskMapper {
    List<SysTask> selectAllAbledTask();

    List<String> selectTableNames();

    List<HashMap> queryAllSysParameter();
}
