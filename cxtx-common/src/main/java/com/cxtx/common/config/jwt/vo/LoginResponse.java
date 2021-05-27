package com.cxtx.common.config.jwt.vo;

import lombok.Data;

@Data
public class LoginResponse {
    private String userId;
    private String username;
    private String token;
    private String refreshToken;


}
