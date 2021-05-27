package com.cxtx.sce_manage.web.rest;

import com.cxtx.common.unit.ResponseUtil;
import com.github.pagehelper.PageInfo;

import com.cxtx.sce_manage.domain.SysRestLog;
import com.cxtx.sce_manage.service.SysRestLogService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.Map;


@Api(tags = "系统日志")
@RestController
@RequestMapping("/api")
public class SysRestLogResource {

    private final Logger log = LoggerFactory.getLogger(SysRestLogResource.class);

    private static final String ENTITY_NAME = "sysRestLog";

    @Autowired
    private SysRestLogService sysRestLogService;

    /**
     * POST  /sysRestLogs  : Creates a new sysRestLog.
     *
     * @param sysRestLog the sysRestLog to create
     * @return the ResponseEntity with status 200
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "新增__SysRestLog__", notes = "新增一个__SysRestLog__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "sysRestLog", value = "__SysRestLog__", required = true, paramType = "body", dataType = "SysRestLog")
    @PostMapping("/sysRestLogs")
    public ResponseEntity<Map> createSysRestLog(@Valid @RequestBody SysRestLog sysRestLog) throws URISyntaxException {
        log.debug("REST request to save SysRestLog : {}", sysRestLog);
        try {
            int result = sysRestLogService.insertSelective(sysRestLog);
            if (result == 1) {
                return ResponseUtil.success("");
            } else {
                return ResponseUtil.error("新增失败！");
            }
        }catch (Exception e){
        return ResponseUtil.error(e.getMessage());
        }
    }

    /**
     * PUT  /sysRestLogs : Updates an existing SysRestLog.
     *
     * @param sysRestLog the sysRestLog to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sysRestLog,
     * or with status 500 (Internal Server Error) if the sysRestLog couldn't be updated
     */
    @ApiOperation(value = "更新__SysRestLog__", notes = "根据指定wid 更新一个__SysRestLog__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "sysRestLog", value = "__SysRestLog__", required = true, paramType = "body", dataType = "SysRestLog")
    @PutMapping("/sysRestLogs")
    public ResponseEntity<Map> updateSysRestLog(@Valid @RequestBody SysRestLog sysRestLog) {
        log.debug("REST request to update SysRestLog : {}", sysRestLog);
        try {
            int result = sysRestLogService.updateByPrimaryKeySelective(sysRestLog);
            if (result == 1) {
                return ResponseUtil.success("");
            } else {
                return ResponseUtil.error("更新失败");
            }
        }catch (Exception e){
            return ResponseUtil.error(e.getMessage());
        }
    }

    /**
     * GET  /sysRestLogs : get all sysRestLogs.
     *
     * @return the ResponseEntity with status 200 (OK) and with body all sysRestLogs
     */
    @GetMapping("/sysRestLogs")
    @ApiOperation(value = "分页获取__SysRestLog__", notes = "分页获取__SysRestLog__列表", response = ResponseUtil.Response.class)
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "pageNum", value = "分页参数：第几页", required = true, paramType = "query", dataType = "int"),
                    @ApiImplicitParam(name = "pageSize", value = "分页参数：每页数量", required = true, paramType = "query", dataType = "int")
            }
    )
    public ResponseEntity<Map> queryByPageSysRestLogs(@ApiIgnore @RequestParam Map params) {
        try {
            PageInfo<SysRestLog> result = sysRestLogService.queryByPage(params);
            if (result != null) {
                return ResponseUtil.success(result);
            } else {
                return ResponseUtil.error("有错误");
            }
        }catch (Exception e){
            return ResponseUtil.error(e.getMessage());
        }
    }


    /**
     * GET  /sysRestLogs/id : get  sysRestLog by id.
     *
     * @return the ResponseEntity with status 200 (OK) and with body all sysRestLogs
     */
    @ApiOperation(value = "获取__SysRestLog__", notes = "根据wid 获取一个__SysRestLog__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "wid", value = "wid", required = true, paramType = "path")
    @GetMapping("/sysRestLogs/{wid}")
    public ResponseEntity<Map> getSysRestLog(@PathVariable String wid) {
        SysRestLog result = sysRestLogService.selectByPrimaryKey(wid);
        if (result != null) {
            return ResponseUtil.success(result);
        } else {
            return ResponseUtil.error("获取失败！");
        }
    }

    /**
     * DELETE /sysRestLogs/:login : delete the "login" SysRestLog.
     *
     * @param wid the login of the sysRestLog to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiOperation(value = "删除__SysRestLog__", notes = "根据wid 删除一个__SysRestLog__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "wid", value = "wid", required = true, paramType = "path")
    @DeleteMapping("/sysRestLogs/{wid}")
    public ResponseEntity<Map> deleteSysRestLog(@PathVariable String wid) {
        log.debug("REST request to delete SysRestLog: {}", wid);
        int result = sysRestLogService.deleteByPrimaryKey(wid);
        if(result == 1){
            return ResponseUtil.success("删除成功！");
        }else{
            return ResponseUtil.error("删除失败！");
        }
    }

}
