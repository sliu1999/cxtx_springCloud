package com.cxtx.user_manage.domain;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class OaFormModel {
    private Long id;

    private String formType;

    private String name;

    private String tableKey;

    private String detailKeys;

    private Long type;

    private Long sort;

    private Integer status;

    private Long createBy;

    private Date createDate;

    private Long updateBy;

    private Date updateDate;

    private String remarks;

    private String tableSchema;

    private String formView;


    private Map<String,Object> startNodeConfig;


}