package com.cxtx.user_manage.web.rest;

import com.cxtx.common.unit.ResponseUtil;
import com.github.pagehelper.PageInfo;

import com.cxtx.user_manage.domain.OaProcessRun;
import com.cxtx.user_manage.service.OaProcessRunService;
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


@Api(tags = "OaProcessRun")
@RestController
@RequestMapping("/api")
public class OaProcessRunResource {

    private final Logger log = LoggerFactory.getLogger(OaProcessRunResource.class);

    private static final String ENTITY_NAME = "oaProcessRun";

    @Autowired
    private OaProcessRunService oaProcessRunService;

    @ApiOperation(value = "新增__OaProcessRun__", notes = "新增一个__OaProcessRun__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "oaProcessRun", value = "__OaProcessRun__", required = true, paramType = "body", dataType = "OaProcessRun")
    @PostMapping("/oaProcessRuns")
    public ResponseEntity<Map> createOaProcessRun(@Valid @RequestBody OaProcessRun oaProcessRun) throws URISyntaxException {
        log.debug("REST request to save OaProcessRun : {}", oaProcessRun);
        try {
            int result = oaProcessRunService.insertSelective(oaProcessRun);
            if (result == 1) {
                return ResponseUtil.success("");
            } else {
                return ResponseUtil.error("新增失败！");
            }
        }catch (Exception e){
        return ResponseUtil.error(e.getMessage());
        }
    }


    @ApiOperation(value = "更新__OaProcessRun__", notes = "根据指定wid 更新一个__OaProcessRun__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "oaProcessRun", value = "__OaProcessRun__", required = true, paramType = "body", dataType = "OaProcessRun")
    @PutMapping("/oaProcessRuns")
    public ResponseEntity<Map> updateOaProcessRun(@Valid @RequestBody OaProcessRun oaProcessRun) {
        log.debug("REST request to update OaProcessRun : {}", oaProcessRun);
        try {
            int result = oaProcessRunService.updateByPrimaryKeySelective(oaProcessRun);
            if (result == 1) {
                return ResponseUtil.success("");
            } else {
                return ResponseUtil.error("更新失败");
            }
        }catch (Exception e){
            return ResponseUtil.error(e.getMessage());
        }
    }


    @GetMapping("/oaProcessRuns")
    @ApiOperation(value = "分页获取__OaProcessRun__", notes = "分页获取__OaProcessRun__列表", response = ResponseUtil.Response.class)
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "pageNum", value = "分页参数：第几页", required = true, paramType = "query", dataType = "int"),
                    @ApiImplicitParam(name = "pageSize", value = "分页参数：每页数量", required = true, paramType = "query", dataType = "int")
            }
    )
    public ResponseEntity<Map> queryByPageOaProcessRuns(@ApiIgnore @RequestParam Map params) {
        try {
            PageInfo<Map<String,Object>> result = oaProcessRunService.queryByPage(params);
            if (result != null) {
                return ResponseUtil.success(result);
            } else {
                return ResponseUtil.error("有错误");
            }
        }catch (Exception e){
            return ResponseUtil.error(e.getMessage());
        }
    }


    @ApiOperation(value = "获取__OaProcessRun__", notes = "根据wid 获取一个__OaProcessRun__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    @GetMapping("/oaProcessRuns/{wid}")
    public ResponseEntity<Map> getOaProcessRun(@PathVariable Long id) {
        OaProcessRun result = oaProcessRunService.selectByPrimaryKey(id);
        if (result != null) {
            return ResponseUtil.success(result);
        } else {
            return ResponseUtil.error("获取失败！");
        }
    }


    @ApiOperation(value = "删除__OaProcessRun__", notes = "根据wid 删除一个__OaProcessRun__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    @DeleteMapping("/oaProcessRuns/{id}")
    public ResponseEntity<Map> deleteOaProcessRun(@PathVariable Long id) {
        log.debug("REST request to delete OaProcessRun: {}", id);
        int result = oaProcessRunService.deleteByPrimaryKey(id);
        if(result == 1){
            return ResponseUtil.success("删除成功！");
        }else{
            return ResponseUtil.error("删除失败！");
        }
    }

}
