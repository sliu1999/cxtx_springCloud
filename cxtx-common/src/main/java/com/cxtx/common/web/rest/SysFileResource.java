package com.cxtx.common.web.rest;

import com.cxtx.common.domain.FileInfo;
import com.cxtx.common.service.SysFileService;
import com.cxtx.common.unit.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SysFileResource {

    @Autowired
    SysFileService sysFileService;


    @PostMapping("/uploadImg")
    @ApiOperation(value = "上传图片", notes = "上传图片")
    public ResponseEntity<?> uploadImg(@RequestParam(value = "file",required=false) MultipartFile file){
        try {
            FileInfo result = sysFileService.uploadImg(file);
            return ResponseUtil.success(result);
        }catch (Exception e){
            return ResponseUtil.error(e.getMessage());
        }
    }

    /**
     * 批量上传图片
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/batchUploadImg")
    @ApiOperation(value = "批量上传图片", notes = "批量上传图片")
    public ResponseEntity<?> batchUploadImg(@RequestParam(value = "file",required=false) MultipartFile[] file) throws Exception {
        try {
            List<FileInfo> result = sysFileService.batchUploadImg(file);
            return ResponseUtil.success(result);
        }catch (Exception e){
            return ResponseUtil.error(e.getMessage());
        }
    }
}
