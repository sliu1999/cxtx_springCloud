package com.cxtx.user_manage.web.rest;

import com.cxtx.common.unit.ResponseUtil;
import com.github.pagehelper.PageInfo;

import com.cxtx.user_manage.domain.OaProcessLog;
import com.cxtx.user_manage.service.OaProcessLogService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.Map;


@Api(tags = "OaProcessLog")
@RestController
@RequestMapping("/api")
public class OaProcessLogResource {

    private final Logger log = LoggerFactory.getLogger(OaProcessLogResource.class);

    private static final String ENTITY_NAME = "oaProcessLog";

    @Autowired
    private OaProcessLogService oaProcessLogService;


    @ApiOperation(value = "新增__OaProcessLog__", notes = "新增一个__OaProcessLog__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "oaProcessLog", value = "__OaProcessLog__", required = true, paramType = "body", dataType = "OaProcessLog")
    @PostMapping("/oaProcessLogs")
    public ResponseEntity<Map> createOaProcessLog(@Valid @RequestBody OaProcessLog oaProcessLog) throws URISyntaxException {
        log.debug("REST request to save OaProcessLog : {}", oaProcessLog);
        try {
            int result = oaProcessLogService.insertSelective(oaProcessLog);
            if (result == 1) {
                return ResponseUtil.success("");
            } else {
                return ResponseUtil.error("新增失败！");
            }
        }catch (Exception e){
        return ResponseUtil.error(e.getMessage());
        }
    }


    @ApiOperation(value = "更新__OaProcessLog__", notes = "根据指定id 更新一个__OaProcessLog__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "oaProcessLog", value = "__OaProcessLog__", required = true, paramType = "body", dataType = "OaProcessLog")
    @PutMapping("/oaProcessLogs")
    public ResponseEntity<Map> updateOaProcessLog(@Valid @RequestBody OaProcessLog oaProcessLog) {
        log.debug("REST request to update OaProcessLog : {}", oaProcessLog);
        try {
            int result = oaProcessLogService.updateByPrimaryKeySelective(oaProcessLog);
            if (result == 1) {
                return ResponseUtil.success("");
            } else {
                return ResponseUtil.error("更新失败");
            }
        }catch (Exception e){
            return ResponseUtil.error(e.getMessage());
        }
    }


    @GetMapping("/oaProcessLogs")
    @ApiOperation(value = "分页获取__OaProcessLog__", notes = "分页获取__OaProcessLog__列表", response = ResponseUtil.Response.class)
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "pageNum", value = "分页参数：第几页", required = true, paramType = "query", dataType = "int"),
                    @ApiImplicitParam(name = "pageSize", value = "分页参数：每页数量", required = true, paramType = "query", dataType = "int")
            }
    )
    public ResponseEntity<Map> queryByPageOaProcessLogs(@ApiIgnore @RequestParam Map params) {
        try {
            PageInfo<OaProcessLog> result = oaProcessLogService.queryByPage(params);
            if (result != null) {
                return ResponseUtil.success(result);
            } else {
                return ResponseUtil.error("有错误");
            }
        }catch (Exception e){
            return ResponseUtil.error(e.getMessage());
        }
    }



    @ApiOperation(value = "获取__OaProcessLog__", notes = "根据id 获取一个__OaProcessLog__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    @GetMapping("/oaProcessLogs/{id}")
    public ResponseEntity<Map> getOaProcessLog(@PathVariable Long id) {

            return ResponseUtil.error("获取失败！");

    }

    @ApiOperation(value = "删除__OaProcessLog__", notes = "根据id 删除一个__OaProcessLog__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    @DeleteMapping("/oaProcessLogs/{id}")
    public ResponseEntity<Map> deleteOaProcessLog(@PathVariable Long id) {
        log.debug("REST request to delete OaProcessLog: {}", id);
        int result = oaProcessLogService.deleteByPrimaryKey(id);
        if(result == 1){
            return ResponseUtil.success("删除成功！");
        }else{
            return ResponseUtil.error("删除失败！");
        }
    }

}
