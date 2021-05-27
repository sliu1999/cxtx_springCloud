package com.cxtx.common.service.impl;

import com.cxtx.common.domain.FileInfo;
import com.cxtx.common.service.SysFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = {Exception.class})
public class SysFileImpl implements SysFileService {

    @Value("${upload-path}")
    String uploadPath;

    @Override
    public FileInfo uploadImg(MultipartFile file) throws IOException {
        String currentTime = new SimpleDateFormat("yyyyMMddHHmmssSSSS").format(new Date());
        String trueFileName = file.getOriginalFilename();
        String suffix = trueFileName.substring(trueFileName.lastIndexOf(".") + 1);
        String fileName = currentTime + "." + suffix;
        // 默认文件夹
        String defaultDir = "/pc/";
        File dirCheck = new File(uploadPath + defaultDir);
        if (!dirCheck.exists()) {
            dirCheck.mkdirs();
        }
        file.transferTo(new File(uploadPath + defaultDir + fileName));

        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(fileName);
        fileInfo.setSaveName(trueFileName);
        fileInfo.setFullUrl(defaultDir);
        return fileInfo;
    }

    @Override
    public List<FileInfo> batchUploadImg(MultipartFile[] file) throws IOException {
        if ( file != null && file.length > 0) {
            for (int i = 0; i < file.length; i++) {

                String currentTime = new SimpleDateFormat("yyyyMMddHHmmssSSSS").format(new Date());
                String trueFileName = file[i].getOriginalFilename();
                String suffix = trueFileName.substring(trueFileName.lastIndexOf(".") + 1);
                String fileName = currentTime + "." + suffix;
                // 默认文件夹
                String defaultDir = "/pc/";
                File dirCheck = new File(uploadPath + defaultDir);
                if (!dirCheck.exists()) {
                    dirCheck.mkdirs();
                }
                file[i].transferTo(new File(uploadPath + defaultDir + fileName));
            }
        }
        return null;
    }
}
