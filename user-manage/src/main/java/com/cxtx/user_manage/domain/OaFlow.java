package com.cxtx.user_manage.domain;

import lombok.Data;

import java.util.Date;

@Data
public class OaFlow {
    private Long id;

    private String flowName;

    private String flowType;

    private Long sort;

    private Integer status;

    private Long createBy;

    private Date createDate;

    private Long updateBy;

    private Date updateDate;

    private String remarks;

    private String height;

    private String width;

}