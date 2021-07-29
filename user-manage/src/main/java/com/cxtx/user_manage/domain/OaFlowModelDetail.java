package com.cxtx.user_manage.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    private Long updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateDate;

    private String remarks;


}