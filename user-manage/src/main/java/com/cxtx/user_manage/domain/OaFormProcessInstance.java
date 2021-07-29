package com.cxtx.user_manage.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class OaFormProcessInstance {
    private Long id;

    private Long appFormId;

    private String processName;

    private Long processId;

    private Long userId;

    private Long formModelId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    private Long createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateDate;

    private Long updateBy;

    private Integer sort;

    private Integer status;

    private String remarks;

}