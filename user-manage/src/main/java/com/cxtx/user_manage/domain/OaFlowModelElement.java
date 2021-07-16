package com.cxtx.user_manage.domain;

import lombok.Data;

import java.util.Date;

@Data
public class OaFlowModelElement {
    private Long id;

    private Long modelId;

    private String code;

    private String name;

    private Long sort;

    private Integer status;

    private Long createBy;

    private Date createDate;

    private Long updateBy;

    private Date updateDate;

    private String remarks;

    private String startX;

    private String startY;

    private String height;

    private String width;


}