package com.cxtx.user_manage.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class OaProcess {
    private Long id;

    private Long createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    private Long updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateDate;

    private Integer sort;

    private Integer status;

    private String remarks;

    private Long flowId;

    private String processName;

    private String codeName;

    private String code;

    @ApiModelProperty("submit数据0待审批，1已拒绝，2待重审，3待提交，4流程到结束节点完成，5已撤回")
    private Integer state;

    private Long appFormId;

    private Long flowModelId;

    private String assignee;


}