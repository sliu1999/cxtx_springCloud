//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cxtx.user_manage.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(
        description = "系统信息"
)
@Data
public class SysInfo {
    @ApiModelProperty("主键，新增时不传")
    private String id;
    @ApiModelProperty(
            value = "中文名",
            example = "测试系统",
            required = true
    )
    private String cnName;
    @ApiModelProperty(
            value = "英文名",
            example = "test system",
            required = true
    )
    private String enName;
    @ApiModelProperty(
            value = "系统ip",
            example = "192.168.1.1"
    )
    private String ip;
    @ApiModelProperty(
            value = "系统端口",
            example = "8080"
    )
    private String port;
    @ApiModelProperty(
            value = "版本号",
            example = "0.0.1"
    )
    private String version;
    @ApiModelProperty(
            value = "版权信息",
            example = "2020 ***信息技术有限公司",
            required = true
    )
    private String copyRight;
    @ApiModelProperty(
            value = "系统类型(1、PC版，2、手机版，3、pad版)",
            allowableValues = "1,2,3",
            required = true
    )
    private Long sysType;
    @ApiModelProperty(
            hidden = true
    )
    private String sysTypeName;
    @ApiModelProperty(
            value = "访问地址",
            example = "http://",
            required = true
    )
    private String url;


}
