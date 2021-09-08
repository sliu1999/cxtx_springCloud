package com.cxtx.common.mapper;

import com.cxtx.common.domain.SysRestLog;

import java.util.List;
import java.util.Map;

public interface SysRestLogMapper {
    int insert(SysRestLog var1);

    List<SysRestLog> selectByCondition(Map var1);
}
