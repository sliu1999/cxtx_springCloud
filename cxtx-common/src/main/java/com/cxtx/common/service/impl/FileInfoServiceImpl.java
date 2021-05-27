
package com.cxtx.common.service.impl;

import com.cxtx.common.domain.FileInfo;
import com.cxtx.common.mapper.FileUploadMapper;
import com.cxtx.common.service.FileInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Service
public class FileInfoServiceImpl implements FileInfoService {
    private final Logger log = LoggerFactory.getLogger(FileInfoServiceImpl.class);
    @Autowired
    FileUploadMapper fileUploadMapper;

    public FileInfoServiceImpl() {
    }

    @Transactional(
            rollbackFor = {RuntimeException.class, Exception.class}
    )
    @Override
    public Integer saveFilesInfo(String tableId, String mainId, List<FileInfo> files) {
        String tableName = this.fileUploadMapper.queryTableNameById(tableId);
        HashMap<String, Object> params = new HashMap(16);
        params.put("tableName", tableName);
        params.put("mainId", mainId);
        params.put("files", files);
        List<FileInfo> delList = this.fileUploadMapper.queryDeleteFilesInfo(params);
        this.fileUploadMapper.deleteFileInfo(params);
        Integer integer = 0;
        if (files != null && files.size() > 0) {
            integer = this.fileUploadMapper.saveFilesInfo(params);
            if (delList != null && delList.size() > 0) {
                Iterator var8 = delList.iterator();

                while(var8.hasNext()) {
                    FileInfo delfileInfo = (FileInfo)var8.next();
                    //SimsFileClient.deleteFile(delfileInfo.getSaveName(), delfileInfo.getSystemName(), delfileInfo.getDir());
                }
            }

            //SimsFileClient.batchSaveFile(files);
        }

        return integer > 0 ? integer : null;
    }

    @Transactional(
            rollbackFor = {RuntimeException.class, Exception.class}
    )
    @Override
    public Integer deleteFileInfo(String tableId, String mainId) {
        String tableName = this.fileUploadMapper.queryTableNameById(tableId);
        HashMap<String, Object> params = new HashMap(16);
        params.put("mainId", mainId);
        params.put("tableName", tableName);
        List<FileInfo> fileInfos = this.fileUploadMapper.queryFilesInfo(params);
        Integer integer = this.fileUploadMapper.deleteFileInfo(params);
        if (integer <= 0) {
            return null;
        } else {
            Iterator var7 = fileInfos.iterator();

            while(var7.hasNext()) {
                FileInfo fileInfo = (FileInfo)var7.next();
                //SimsFileClient.deleteFile(fileInfo.getSaveName(), fileInfo.getSystemName(), fileInfo.getDir());
            }

            return integer;
        }
    }

    @Override
    public List<FileInfo> queryFilesInfoByMainId(String tableId, String mainId) {
        String tableName = this.fileUploadMapper.queryTableNameById(tableId);
        HashMap<String, Object> params = new HashMap(16);
        params.put("mainId", mainId);
        params.put("tableName", tableName);
        List<FileInfo> fileInfos = this.fileUploadMapper.queryFilesInfoByMainId(params);
        return fileInfos;
    }
}
