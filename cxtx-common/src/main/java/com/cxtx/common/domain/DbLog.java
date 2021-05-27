package com.cxtx.common.domain;

import lombok.Data;

import java.util.Date;

@Data
public class DbLog {
    private Integer id;
    private String fileName;
    private Date operateDate;
}
