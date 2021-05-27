//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cxtx.user_manage.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(
        description = "部门"
)
@Data
public class Department {
    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("部门名称")
    private String name;
    @ApiModelProperty("上级部门编码")
    private String parentId;
    @ApiModelProperty("上级部门名称")
    private String parentName;
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("部门领导")
    private String leader;
    private String leaderName;
    private Integer staffCount;

}
