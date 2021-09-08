package com.cxtx.common.service;

import com.cxtx.common.domain.SysRestLog;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface SysRestLogService {
    int insert(SysRestLog var1);

    PageInfo<SysRestLog> queryByPage(Map var1);

    List<SysRestLog> selectAll(Map var1);
}
