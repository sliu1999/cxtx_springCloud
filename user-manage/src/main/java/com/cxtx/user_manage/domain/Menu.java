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
        description = "菜单"
)
@Data
public class Menu {




    @ApiModelProperty("主键")
    private String id;
    private String parentId;
    @ApiModelProperty("名称")
    private String name;

    private String path;
    private String component;
    private String redirect;
    private String title;
    private String icon;
    private String hidden;

    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("父节点名称")
    private String parentName;
    private List<Menu> subMenus;
    @ApiModelProperty("绑定此菜单的所有角色")
    private String useRoles;


}
