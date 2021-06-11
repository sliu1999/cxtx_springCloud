package com.cxtx.gateway.domain;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtModel {
    private String userId;
    private String username;

    @Override
    public String toString() {
        JwtModel jwtModel = new JwtModel();
        jwtModel.setUserId(userId);
        jwtModel.setUsername(username);
        return JSONObject.toJSONString(jwtModel);
    }
}
