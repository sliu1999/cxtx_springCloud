package com.cxtx.auth_service.mapper;

import com.cxtx.common.config.jwt.vo.LoginRequest;
import com.cxtx.common.domain.JwtModel;
import org.mapstruct.Mapper;

@Mapper
public interface AuthUserMapper {

    JwtModel queryUserByPassword(LoginRequest loginRequest);
}
