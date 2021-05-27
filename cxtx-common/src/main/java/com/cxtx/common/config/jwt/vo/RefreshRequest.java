package com.cxtx.common.config.jwt.vo;

import lombok.Data;

@Data
public class RefreshRequest {
    private String userId;
    private String refreshToken;


}
