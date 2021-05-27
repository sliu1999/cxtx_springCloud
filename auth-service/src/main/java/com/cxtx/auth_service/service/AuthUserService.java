package com.cxtx.auth_service.service;

import com.cxtx.common.config.jwt.vo.LoginRequest;
import com.cxtx.common.domain.JwtModel;

public interface AuthUserService {

    JwtModel isUser(LoginRequest loginRequest);
}
