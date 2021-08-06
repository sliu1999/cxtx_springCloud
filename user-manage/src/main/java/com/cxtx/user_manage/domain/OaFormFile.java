package com.cxtx.user_manage.domain;

import lombok.Data;

import java.util.Date;

@Data
public class OaFormFile {
    private Long id;

    private String fileName;

    private String trueName;

    private String fileDesc;

    private String filePath;

    private Long sort;

    private Integer status;

    private Long createBy;

    private Date createDate;

    private String remarks;

    private String createName;

    private String size;

}