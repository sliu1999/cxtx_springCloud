package com.cxtx.user_manage.domain;

import lombok.Data;

import java.util.Date;

@Data
public class OaProcessLog {
    private Long id;

    private Long flowId;

    private Long processId;

    private String curNode;

    private String nextNode;

    private Integer actionId;

    private Long createBy;

    private Date createDate;

    private Long updateBy;

    private Date updateDate;

    private Integer sort;

    private Integer status;

    private String remarks;

    private String curAssignee;

    private String nextAssignees;

    private String message;


}