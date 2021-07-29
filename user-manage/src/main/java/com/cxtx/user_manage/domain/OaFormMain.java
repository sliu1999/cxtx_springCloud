package com.cxtx.user_manage.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class OaFormMain {
    private Long id;

    @ApiModelProperty(value = "配置可见表单，先选表单类别，然后勾选这个类别下表单选择全部type=1,flag=0/排除所选type=0,flag=1(然后选具体排除的表单)" +
            "{\"typeIds\":[\"1\"],\"formIds\":[],\"type\":\"1\",\"flag\":\"0\"} 这个类型全部表单可见" +
            "{\"typeIds\":[\"1\"],\"formIds\":[15017584904110080],\"type\":\"0\",\"flag\":\"1\"} 排除类型1下15017584904110080表单，其他表单可见" +
            "{\"typeIds\":[\"1\"],\"formIds\":[],\"type\":\"0\",\"flag\":\"1\"} 不排除这个类型下表单，即这个类型下所有表单可见" +
            "{\"typeIds\":[\"2\"],\"formIds\":[14176546320809984],\"type\":\"0\",\"flag\":\"0\"} 选择这个类型下某个表单可见")
    private String typesForms;

    private String name;

    @ApiModelProperty(value = "权限组别-人员")
    private String authGroup;

    @ApiModelProperty(value = "是否勾选全部人员可用，0否，1是")
    private String authAll;

    private Long sort;

    private Integer status;

    private Long createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    private Long updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateDate;

    private String remarks;


}