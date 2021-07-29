package com.cxtx.user_manage.web.rest;

import com.cxtx.common.unit.ResponseUtil;
import com.github.pagehelper.PageInfo;

import com.cxtx.user_manage.domain.OaFormProcessInstance;
import com.cxtx.user_manage.service.OaFormProcessInstanceService;
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


@Api(tags = "OaFormProcessInstance")
@RestController
@RequestMapping("/api")
public class OaFormProcessInstanceResource {

    private final Logger log = LoggerFactory.getLogger(OaFormProcessInstanceResource.class);

    private static final String ENTITY_NAME = "oaFormProcessInstance";

    @Autowired
    private OaFormProcessInstanceService oaFormProcessInstanceService;


    @ApiOperation(value = "新增__OaFormProcessInstance__", notes = "新增一个__OaFormProcessInstance__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "oaFormProcessInstance", value = "__OaFormProcessInstance__", required = true, paramType = "body", dataType = "OaFormProcessInstance")
    @PostMapping("/oaFormProcessInstances")
    public ResponseEntity<Map> createOaFormProcessInstance(@Valid @RequestBody OaFormProcessInstance oaFormProcessInstance) throws URISyntaxException {
        log.debug("REST request to save OaFormProcessInstance : {}", oaFormProcessInstance);
        try {
            int result = oaFormProcessInstanceService.insertSelective(oaFormProcessInstance);
            if (result == 1) {
                return ResponseUtil.success("");
            } else {
                return ResponseUtil.error("新增失败！");
            }
        }catch (Exception e){
        return ResponseUtil.error(e.getMessage());
        }
    }


    @ApiOperation(value = "更新__OaFormProcessInstance__", notes = "根据指定wid 更新一个__OaFormProcessInstance__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "oaFormProcessInstance", value = "__OaFormProcessInstance__", required = true, paramType = "body", dataType = "OaFormProcessInstance")
    @PutMapping("/oaFormProcessInstances")
    public ResponseEntity<Map> updateOaFormProcessInstance(@Valid @RequestBody OaFormProcessInstance oaFormProcessInstance) {
        log.debug("REST request to update OaFormProcessInstance : {}", oaFormProcessInstance);
        try {
            int result = oaFormProcessInstanceService.updateByPrimaryKeySelective(oaFormProcessInstance);
            if (result == 1) {
                return ResponseUtil.success("");
            } else {
                return ResponseUtil.error("更新失败");
            }
        }catch (Exception e){
            return ResponseUtil.error(e.getMessage());
        }
    }


    @GetMapping("/oaFormProcessInstances")
    @ApiOperation(value = "分页获取__OaFormProcessInstance__", notes = "分页获取__OaFormProcessInstance__列表", response = ResponseUtil.Response.class)
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "pageNum", value = "分页参数：第几页", required = true, paramType = "query", dataType = "int"),
                    @ApiImplicitParam(name = "pageSize", value = "分页参数：每页数量", required = true, paramType = "query", dataType = "int")
            }
    )
    public ResponseEntity<Map> queryByPageOaFormProcessInstances(@ApiIgnore @RequestParam Map params) {
        try {
            PageInfo<OaFormProcessInstance> result = oaFormProcessInstanceService.queryByPage(params);
            if (result != null) {
                return ResponseUtil.success(result);
            } else {
                return ResponseUtil.error("有错误");
            }
        }catch (Exception e){
            return ResponseUtil.error(e.getMessage());
        }
    }



    @ApiOperation(value = "获取__OaFormProcessInstance__", notes = "根据wid 获取一个__OaFormProcessInstance__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    @GetMapping("/oaFormProcessInstances/{id}")
    public ResponseEntity<Map> getOaFormProcessInstance(@PathVariable Long id) {
        OaFormProcessInstance result = oaFormProcessInstanceService.selectByPrimaryKey(id);
        if (result != null) {
            return ResponseUtil.success(result);
        } else {
            return ResponseUtil.error("获取失败！");
        }
    }


    @ApiOperation(value = "删除__OaFormProcessInstance__", notes = "根据wid 删除一个__OaFormProcessInstance__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    @DeleteMapping("/oaFormProcessInstances/{id}")
    public ResponseEntity<Map> deleteOaFormProcessInstance(@PathVariable Long id) {
        log.debug("REST request to delete OaFormProcessInstance: {}", id);
        int result = oaFormProcessInstanceService.deleteByPrimaryKey(id);
        if(result == 1){
            return ResponseUtil.success("删除成功！");
        }else{
            return ResponseUtil.error("删除失败！");
        }
    }

}
