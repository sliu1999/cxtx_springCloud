package com.cxtx.auth_service.service.impl;

import com.cxtx.auth_service.mapper.AuthUserMapper;
import com.cxtx.auth_service.service.AuthUserService;
import com.cxtx.common.config.jwt.vo.LoginRequest;
import com.cxtx.common.domain.JwtModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(rollbackFor = Exception.class)
public class AuthUserServiceImpl implements AuthUserService {

    @Autowired
    private AuthUserMapper authUserMapper;

    @Override
    public JwtModel isUser(LoginRequest loginRequest) {
        JwtModel jwtModel = authUserMapper.queryUserByPassword(loginRequest);
        return jwtModel;
    }
}
