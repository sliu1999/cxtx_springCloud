package com.cxtx.user_manage.domain;

import lombok.Data;

import java.util.Date;

@Data
public class OaFlowModelDetail {
    private Long id;

    private Long modelId;

    private String lastNodeCode;

    private String nodeCode;

    private String nextNodeCode;

    private String name;

    private Long sort;

    private Integer status;

    private Long createBy;

    private Date createDate;

    private Long updateBy;

    private Date updateDate;

    private String remarks;


}