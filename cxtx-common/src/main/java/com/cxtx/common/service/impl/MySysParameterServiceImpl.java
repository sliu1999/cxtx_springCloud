package com.cxtx.common.service.impl;

import com.cxtx.common.domain.MySysParameter;
import com.cxtx.common.mapper.MySysParameterMapper;
import com.cxtx.common.service.MySysParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
@Transactional(
        rollbackFor = {Exception.class}
)
public class MySysParameterServiceImpl implements MySysParameterService {
    @Autowired
    private MySysParameterMapper sysParameterMapper;


    @Override
    public List<MySysParameter> selectAllList() {
        return this.sysParameterMapper.selectSysParameterListByCondition(new HashMap(16));
    }

    @Override
    public Boolean isEnabled() {
        List<String> tables = this.sysParameterMapper.selectTableNames();
        return tables.size() == 1 ? true : false;
    }
}

