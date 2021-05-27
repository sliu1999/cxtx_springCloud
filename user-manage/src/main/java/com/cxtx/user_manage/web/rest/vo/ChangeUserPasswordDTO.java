

package com.cxtx.user_manage.web.rest.vo;

import com.alibaba.fastjson.JSONObject;

import com.cxtx.common.unit.Md5;
import io.swagger.annotations.ApiModelProperty;

public class ChangeUserPasswordDTO {
    private String id;
    private String password;
    @ApiModelProperty("loginId")
    private String loginId;

    public ChangeUserPasswordDTO() {
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return Md5.md5Encode(this.password);
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
