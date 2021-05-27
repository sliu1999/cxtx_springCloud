package com.cxtx.common.service;

import com.cxtx.common.domain.MySysParameter;

import java.util.List;

public interface MySysParameterService {
    List<MySysParameter> selectAllList();

    Boolean isEnabled();
}
