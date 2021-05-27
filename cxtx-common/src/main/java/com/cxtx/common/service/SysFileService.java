package com.cxtx.common.service;

import com.cxtx.common.domain.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SysFileService {
    FileInfo uploadImg(MultipartFile file) throws IOException;

    List<FileInfo> batchUploadImg(MultipartFile [] file) throws IOException;
}
