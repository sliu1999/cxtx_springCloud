package com.cxtx.user_manage.domain;

import lombok.Data;

import java.util.Date;

@Data
public class OaProcess {
    private Long id;

    private Long createBy;

    private Date createDate;

    private Long updateBy;

    private Date updateDate;

    private Integer sort;

    private Integer status;

    private String remarks;

    private Long flowId;

    private String processName;

    private String codeName;

    private String code;

    private Integer state;

    private Long formId;

    private Long flowModelId;

    private String assignee;


}