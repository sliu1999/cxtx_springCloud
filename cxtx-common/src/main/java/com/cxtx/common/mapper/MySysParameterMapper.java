package com.cxtx.common.mapper;

import com.cxtx.common.domain.MySysParameter;

import java.util.List;
import java.util.Map;

public interface MySysParameterMapper {
    List<MySysParameter> selectSysParameterListByCondition(Map var1);

    List<String> selectTableNames();
}
