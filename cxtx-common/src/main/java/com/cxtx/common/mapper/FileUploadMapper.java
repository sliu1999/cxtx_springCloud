//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cxtx.common.mapper;

import com.cxtx.common.domain.FileInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface FileUploadMapper {
    List<FileInfo> queryFilesInfo(HashMap<String, Object> var1);

    String queryTableNameById(String var1);

    int saveFilesInfo(Map var1);

    int deleteFileInfo(Map var1);

    List<FileInfo> queryDeleteFilesInfo(HashMap<String, Object> var1);

    List<FileInfo> queryFilesInfoByMainId(HashMap<String, Object> var1);
}
