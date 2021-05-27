//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cxtx.user_manage.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@ApiModel(
        description = "用户"
)
@Data
public class User {
    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("账号")
    private String loginId;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    @ApiModelProperty("在线状态")
    private Integer onlineId;
    @ApiModelProperty("在线状态名称")
    private String onlineName;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("用户角色权限")
    private List<Role> roles;
    @ApiModelProperty("用户角色名")
    private String roleNames;
    private Staff staff;


}
