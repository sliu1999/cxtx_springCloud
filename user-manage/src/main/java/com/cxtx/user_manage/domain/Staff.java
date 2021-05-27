//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cxtx.user_manage.domain;

import com.cxtx.common.domain.FileInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(
        description = "员工"
)
@Data
public class Staff {
    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("工号")
    private String staffNo;
    @ApiModelProperty("账号")
    private String loginId;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("电话")
    private String phone;
    @ApiModelProperty("固定电话")
    private String officePhone;
    @ApiModelProperty("入职时间")
    private String entryDate;
    @ApiModelProperty("证件号")
    private String identityCard;
    @ApiModelProperty("地址")
    private String address;
    @ApiModelProperty("生日")
    private String birthday;
    @ApiModelProperty("性别编码")
    private Integer sex;
    @ApiModelProperty("性别")
    private String sexName;
    @ApiModelProperty("员工类型编码：全职/兼职/实习/离职")
    private Integer staffType;
    @ApiModelProperty("员工类型名称：全职/兼职/实习/离职")
    private String staffTypeName;
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("姓名拼音")
    private String pinyin;
    @ApiModelProperty("创建时间")
    private String createTime;
    @ApiModelProperty("更新时间")
    private String updateTime;
    @ApiModelProperty("直属上级")
    private String leader;
    @ApiModelProperty("直属上级姓名")
    private String leaderName;
    @ApiModelProperty("所属部门")
    private List<Department> departments;
    @ApiModelProperty("证件照")
    private List<FileInfo> iconCard;
    @ApiModelProperty("个人头像")
    private List<FileInfo> iconPhoto;
    private String roleNames;
    private String departmentNames;

}
