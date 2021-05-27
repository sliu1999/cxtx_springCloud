

package com.cxtx.common.service;

import com.cxtx.common.domain.FileInfo;

import java.util.List;

public interface FileInfoService {
    Integer saveFilesInfo(String var1, String var2, List<FileInfo> var3);

    Integer deleteFileInfo(String var1, String var2);

    List<FileInfo> queryFilesInfoByMainId(String var1, String var2);
}
