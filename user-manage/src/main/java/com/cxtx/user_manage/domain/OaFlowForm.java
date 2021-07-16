package com.cxtx.user_manage.domain;

import lombok.Data;

import java.util.Date;

@Data
public class OaFlowForm {
    private Long id;

    private Long formModelId;

    private Long flowId;

    private Long flowModelId;

    private Integer status;

    private Long createBy;

    private Date createDate;

    private Long updateBy;

    private Date updateDate;

    private Integer sort;

    private String remarks;


}