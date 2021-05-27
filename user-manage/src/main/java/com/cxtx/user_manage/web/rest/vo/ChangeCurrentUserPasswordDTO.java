

package com.cxtx.user_manage.web.rest.vo;

import com.alibaba.fastjson.JSONObject;
import com.cxtx.common.unit.Md5;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class ChangeCurrentUserPasswordDTO {
    @ApiModelProperty("loginId")
    private String loginId;
    @ApiModelProperty("原密码")
    private String oldPassword;
    @ApiModelProperty("新密码")
    private String newPassword;

    public ChangeCurrentUserPasswordDTO() {
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    public String getOldPassword() {
        return Md5.md5Encode(this.oldPassword);
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return Md5.md5Encode(this.newPassword);
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getLoginId() {
        return this.loginId;
    }
}
