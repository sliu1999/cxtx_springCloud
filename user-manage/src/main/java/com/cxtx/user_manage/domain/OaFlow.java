package com.cxtx.user_manage.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class OaFlow {
    private Long id;

    private String flowName;

    private String flowType;

    private Long sort;

    private Integer status;

    private Long createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    private Long updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateDate;

    private String remarks;

    private String height;

    private String width;



    private OaFlowModel processModel;
    private List<OaFlowModelElement> modElements;
    private Map<String,Object> module;
    private List<Map<String,Object>> modSequenceElements;



}
