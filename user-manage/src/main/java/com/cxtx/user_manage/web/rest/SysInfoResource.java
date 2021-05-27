//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cxtx.user_manage.web.rest;

import com.cxtx.user_manage.domain.SysInfo;
import com.cxtx.user_manage.service.SysInfoService;
import com.cxtx.common.unit.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.Map;

@Api(
        tags = {"系统信息"},
        description = "管理系统的基础信息"
)
@RestController
@RequestMapping({"/api"})
public class SysInfoResource {
    private final Logger logger = LoggerFactory.getLogger(SysInfoResource.class);
    private static final String ENTITY_NAME = "sysInfo";
    @Autowired
    private SysInfoService sysInfoService;

    public SysInfoResource() {
    }

    @ApiOperation(
            value = "保存系统基本信息",
            notes = "新增一个系统的基本信息",
            response = ResponseUtil.Response.class
    )
    @ApiImplicitParam(
            name = "sysInfo",
            value = "系统基本信息",
            required = true,
            paramType = "body",
            dataType = "SysInfo"
    )
    @PostMapping({"/sysInfo"})
    public ResponseEntity<Map> createSystem(@Valid @RequestBody SysInfo sysInfo) throws URISyntaxException {
        try {
            int result = this.sysInfoService.saveSysInfo(sysInfo);
            return result == 1 ? ResponseUtil.success("保存成功！") : ResponseUtil.error("保存失败！");
        } catch (Exception var3) {
            return ResponseUtil.error(var3.getMessage());
        }
    }

    @ApiOperation(
            value = "获取系统基本信息",
            notes = "获取系统的基本信息",
            response = ResponseUtil.Response.class
    )
    @GetMapping({"/sysInfo"})
    public ResponseEntity<Map> getSystem() {
        SysInfo result = this.sysInfoService.selectSysInfo();
        return result != null ? ResponseUtil.success(result) : ResponseUtil.error("获取失败！");
    }
}
