package com.cxtx.user_manage.web.rest;

import com.cxtx.common.unit.ResponseUtil;
import com.github.pagehelper.PageInfo;

import com.cxtx.user_manage.domain.OaFormFile;
import com.cxtx.user_manage.service.OaFormFileService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Api(tags = "OaFormFile")
@RestController
@RequestMapping("/api/OaFormFile")
public class OaFormFileResource {

    private final Logger log = LoggerFactory.getLogger(OaFormFileResource.class);

    private static final String ENTITY_NAME = "oaFormFile";

    @Autowired
    private OaFormFileService oaFormFileService;



    @PostMapping("/uploadImg")
    public ResponseEntity<Map> createOaFormFile(@RequestParam(value = "file",required=false) MultipartFile file) throws URISyntaxException {

        try {
            OaFormFile result = oaFormFileService.uploadImg(file);
            if (result != null) {
                return ResponseUtil.success(result);
            } else {
                return ResponseUtil.error("新增失败！");
            }
        }catch (Exception e){
        return ResponseUtil.error(e.getMessage());
        }
    }

    @GetMapping(value = "/list/{ids}")
    @ApiOperation(value = "通过字符串ids获取附件详情，id逗号隔开", notes = "通过IDs获取附件详情")
    public ResponseEntity<Map> getList(@PathVariable(value = "ids") String ids){
        String[] idArr = ids.split(",");
        List<Long> idList = new ArrayList(Arrays.asList(idArr));
        List<OaFormFile> result = oaFormFileService.getListByIds(idList);
        return ResponseUtil.success(result);
    }




}
