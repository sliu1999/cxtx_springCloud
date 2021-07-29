package com.cxtx.user_manage.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class OaFlowModelElement {
    private Long id;

    private Long modelId;

    private String code;

    private String name;

    private Long sort;

    private Integer status;

    private Long createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    private Long updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateDate;

    private String remarks;

    private String startX;

    private String startY;

    private String height;

    private String width;


    @ApiModelProperty(value = "元素参数")
    private String param;



    private Map<String,Object> modElementConfig;


}