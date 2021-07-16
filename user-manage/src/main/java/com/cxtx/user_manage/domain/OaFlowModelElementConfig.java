package com.cxtx.user_manage.domain;

import lombok.Data;

import java.util.Date;

@Data
public class OaFlowModelElementConfig {
    private Long id;

    private Long elementId;

    private Long createBy;

    private Date createDate;

    private Long updateBy;

    private Date updateDate;

    private Integer sort;

    private Integer status;

    private String remarks;

    private String elementConfig;


}