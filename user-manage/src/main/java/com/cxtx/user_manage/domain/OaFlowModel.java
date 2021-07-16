package com.cxtx.user_manage.domain;

import lombok.Data;

import java.util.Date;

@Data
public class OaFlowModel {
    private Long id;

    private Long flowId;

    private String flowName;

    private Long sort;

    private Integer status;

    private Long createBy;

    private Date createDate;

    private Long updateBy;

    private Date updateDate;

    private String remarks;


}