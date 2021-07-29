package com.cxtx.user_manage.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class OaProcessLog {
    private Long id;

    private Long flowId;

    private Long processId;

    private String curNode;

    private String curNodeName;

    private String curAssignee;

    private String curAssigneeName;

    private String nextNode;

    private String nextNodeName;

    private String nextAssignees;

    private String nextAssigneeName;

    @ApiModelProperty("审批状态,1表示发起,2表示通过,3拒绝，4转交，5结束")
    private Integer actionId;

    private Long createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    private Long updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateDate;

    private Integer sort;

    private Integer status;

    private String remarks;

    private String message;



    @ApiModelProperty(value = "评价信息")
    private String evaluatemsg;
    /*转交到某一节点*/
    @ApiModelProperty(value = "转交到某一节点")
    private String toNode;






}