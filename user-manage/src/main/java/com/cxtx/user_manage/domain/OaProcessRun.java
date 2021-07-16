package com.cxtx.user_manage.domain;

import lombok.Data;

import java.util.Date;

@Data
public class OaProcessRun {
    private Long id;

    private Long createBy;

    private Date createDate;

    private Long updateBy;

    private Date updateDate;

    private Integer sort;

    private Integer status;

    private String remarks;

    private Long processId;

    private Long userId;


}