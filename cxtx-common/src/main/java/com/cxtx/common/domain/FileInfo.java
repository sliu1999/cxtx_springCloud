package com.cxtx.common.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FileInfo {
    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("关联id")
    private String mainId;
    @ApiModelProperty("文件名")
    private String fileName;
    @ApiModelProperty("保存文件名")
    private String saveName;
    @ApiModelProperty("url")
    private String fullUrl;
    @ApiModelProperty("上传时间")
    private String uploadTime;
    @ApiModelProperty("所在文件夹")
    private String dir;
    @ApiModelProperty("所属系统名称")
    private String systemName;
    @ApiModelProperty("编号")
    private Integer orderNum;
    @ApiModelProperty("是否为封面")
    private Integer isCover;
    @ApiModelProperty("文件类型")
    private String fileType;

    private String comSaveNames;

}
