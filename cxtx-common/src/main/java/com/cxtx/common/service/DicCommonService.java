package com.cxtx.common.service;

import com.cxtx.common.domain.DicCommon;

import java.util.HashMap;

public interface DicCommonService {
    HashMap<String, Object> selectAll(HashMap<String, Object> var1);

    DicCommon selectOneDic(HashMap<String, Object> var1);
}

