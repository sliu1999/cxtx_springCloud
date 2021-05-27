//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cxtx.user_manage.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(
        description = "角色"
)
@Data
public class Role {
    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("角色菜单权限")
    private List<String> menuIds;
    private Integer isDefault;


}
