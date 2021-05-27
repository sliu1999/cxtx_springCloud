package com.cxtx.common.service.impl;

import com.cxtx.common.domain.DicCommon;
import com.cxtx.common.mapper.DicCommonMapper;
import com.cxtx.common.service.DicCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class DicCommonServiceImpl implements DicCommonService {
    private final Logger log = LoggerFactory.getLogger(DicCommonServiceImpl.class);
    @Autowired
    private DicCommonMapper dicCommonMapper;

    @Override
    public HashMap<String, Object> selectAll(HashMap<String, Object> params) {
        HashMap<String, Object> hm = new HashMap(16);
        List<DicCommon> tbCateManageList = this.dicCommonMapper.selectAll(params);
        int num = this.dicCommonMapper.selectAllCount(params);
        hm.put("total", num);
        hm.put("content", tbCateManageList);
        return hm;
    }
}