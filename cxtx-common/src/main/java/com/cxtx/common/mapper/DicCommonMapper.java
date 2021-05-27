package com.cxtx.common.mapper;

import com.cxtx.common.domain.DicCommon;

import java.util.HashMap;
import java.util.List;

public interface DicCommonMapper {
    List<DicCommon> selectAll(HashMap<String, Object> var1);

    int selectAllCount(HashMap<String, Object> var1);
}
