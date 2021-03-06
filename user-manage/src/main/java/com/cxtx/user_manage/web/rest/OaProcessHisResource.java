package com.cxtx.user_manage.web.rest;


import com.cxtx.common.domain.JwtModel;
import com.cxtx.common.unit.HttpServletUtils;
import com.cxtx.common.unit.ResponseUtil;
import com.github.pagehelper.PageInfo;

import com.cxtx.user_manage.domain.OaProcessHis;
import com.cxtx.user_manage.service.OaProcessHisService;
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


@Api(tags = "OaProcessHis")
@RestController
@RequestMapping("/api")
public class OaProcessHisResource {

    private final Logger log = LoggerFactory.getLogger(OaProcessHisResource.class);

    private static final String ENTITY_NAME = "oaProcessHis";

    @Autowired
    private OaProcessHisService oaProcessHisService;


    @ApiOperation(value = "新增__OaProcessHis__", notes = "新增一个__OaProcessHis__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "oaProcessHis", value = "__OaProcessHis__", required = true, paramType = "body", dataType = "OaProcessHis")
    @PostMapping("/oaProcessHiss")
    public ResponseEntity<Map> createOaProcessHis(@Valid @RequestBody OaProcessHis oaProcessHis) throws URISyntaxException {
        log.debug("REST request to save OaProcessHis : {}", oaProcessHis);
        try {
            int result = oaProcessHisService.insertSelective(oaProcessHis);
            if (result == 1) {
                return ResponseUtil.success("");
            } else {
                return ResponseUtil.error("新增失败！");
            }
        }catch (Exception e){
        return ResponseUtil.error(e.getMessage());
        }
    }


    @ApiOperation(value = "更新__OaProcessHis__", notes = "根据指定wid 更新一个__OaProcessHis__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "oaProcessHis", value = "__OaProcessHis__", required = true, paramType = "body", dataType = "OaProcessHis")
    @PutMapping("/oaProcessHiss")
    public ResponseEntity<Map> updateOaProcessHis(@Valid @RequestBody OaProcessHis oaProcessHis) {
        log.debug("REST request to update OaProcessHis : {}", oaProcessHis);
        try {
            int result = oaProcessHisService.updateByPrimaryKeySelective(oaProcessHis);
            if (result == 1) {
                return ResponseUtil.success("");
            } else {
                return ResponseUtil.error("更新失败");
            }
        }catch (Exception e){
            return ResponseUtil.error(e.getMessage());
        }
    }


    @GetMapping("/oaProcessHiss")
    @ApiOperation(value = "已办事项", notes = "已办事项", response = ResponseUtil.Response.class)
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "pageNum", value = "分页参数：第几页", required = true, paramType = "query", dataType = "int"),
                    @ApiImplicitParam(name = "pageSize", value = "分页参数：每页数量", required = true, paramType = "query", dataType = "int")
            }
    )
    public ResponseEntity<Map> queryByPageOaProcessHiss(@ApiIgnore @RequestParam Map params) {
        try {
            PageInfo<Map<String,Object>> result = oaProcessHisService.queryByPageMap(params);
            if (result != null) {
                return ResponseUtil.success(result);
            } else {
                return ResponseUtil.error("有错误");
            }
        }catch (Exception e){
            return ResponseUtil.error(e.getMessage());
        }
    }



    @ApiOperation(value = "获取__OaProcessHis__", notes = "根据wid 获取一个__OaProcessHis__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    @GetMapping("/oaProcessHiss/{wid}")
    public ResponseEntity<Map> getOaProcessHis(@PathVariable Long id) {
        OaProcessHis result = oaProcessHisService.selectByPrimaryKey(id);
        if (result != null) {
            return ResponseUtil.success(result);
        } else {
            return ResponseUtil.error("获取失败！");
        }
    }

    @ApiOperation(value = "删除__OaProcessHis__", notes = "根据wid 删除一个__OaProcessHis__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    @DeleteMapping("/oaProcessHiss/{id}")
    public ResponseEntity<Map> deleteOaProcessHis(@PathVariable Long id) {
        log.debug("REST request to delete OaProcessHis: {}", id);
        int result = oaProcessHisService.deleteByPrimaryKey(id);
        if(result == 1){
            return ResponseUtil.success("删除成功！");
        }else{
            return ResponseUtil.error("删除失败！");
        }
    }

}
