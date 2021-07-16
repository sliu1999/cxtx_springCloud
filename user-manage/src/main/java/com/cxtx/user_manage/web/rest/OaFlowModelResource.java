package com.cxtx.user_manage.web.rest;


import com.cxtx.common.unit.ResponseUtil;
import com.cxtx.user_manage.domain.OaFormModel;
import com.github.pagehelper.PageInfo;

import com.cxtx.user_manage.domain.OaFlowModel;
import com.cxtx.user_manage.service.OaFlowModelService;

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
import java.util.HashMap;
import java.util.Map;


@Api(tags = "OaFlowModel")
@RestController
@RequestMapping("/api")
public class OaFlowModelResource {

    private final Logger log = LoggerFactory.getLogger(OaFlowModelResource.class);

    private static final String ENTITY_NAME = "oaFlowModel";

    @Autowired
    private OaFlowModelService oaFlowModelService;


    @ApiOperation(value = "新增__OaFlowModel__", notes = "新增一个__OaFlowModel__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "oaFlowModel", value = "__OaFlowModel__", required = true, paramType = "body", dataType = "OaFlowModel")
    @PostMapping("/oaFlowModels")
    public ResponseEntity<Map> createOaFlowModel(@Valid @RequestBody OaFlowModel oaFlowModel) throws URISyntaxException {
        log.debug("REST request to save OaFlowModel : {}", oaFlowModel);
        try {
            int result = oaFlowModelService.insertSelective(oaFlowModel);
            if (result == 1) {
                return ResponseUtil.success("");
            } else {
                return ResponseUtil.error("新增失败！");
            }
        }catch (Exception e){
        return ResponseUtil.error(e.getMessage());
        }
    }


    @ApiOperation(value = "更新__OaFlowModel__", notes = "根据指定wid 更新一个__OaFlowModel__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "oaFlowModel", value = "__OaFlowModel__", required = true, paramType = "body", dataType = "OaFlowModel")
    @PutMapping("/oaFlowModels")
    public ResponseEntity<Map> updateOaFlowModel(@Valid @RequestBody OaFlowModel oaFlowModel) {
        log.debug("REST request to update OaFlowModel : {}", oaFlowModel);
        try {
            int result = oaFlowModelService.updateByPrimaryKeySelective(oaFlowModel);
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
     * GET  /oaFlowModels : get all oaFlowModels.
     *
     * @return the ResponseEntity with status 200 (OK) and with body all oaFlowModels
     */
    @GetMapping("/oaFlowModels")
    @ApiOperation(value = "分页获取__OaFlowModel__", notes = "分页获取__OaFlowModel__列表", response = ResponseUtil.Response.class)
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "pageNum", value = "分页参数：第几页", required = true, paramType = "query", dataType = "int"),
                    @ApiImplicitParam(name = "pageSize", value = "分页参数：每页数量", required = true, paramType = "query", dataType = "int")
            }
    )
    public ResponseEntity<Map> queryByPageOaFlowModels(@ApiIgnore @RequestParam Map params) {
        try {
            PageInfo<OaFlowModel> result = oaFlowModelService.queryByPage(params);
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
     * GET  /oaFlowModels/id : get  oaFlowModel by id.
     *
     * @return the ResponseEntity with status 200 (OK) and with body all oaFlowModels
     */
    @ApiOperation(value = "获取__OaFlowModel__", notes = "根据wid 获取一个__OaFlowModel__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    @GetMapping("/oaFlowModels/{id}")
    public ResponseEntity<Map> getOaFlowModel(@PathVariable Long id) {
        OaFlowModel result = oaFlowModelService.selectByPrimaryKey(id);
        if (result != null) {
            return ResponseUtil.success(result);
        } else {
            return ResponseUtil.error("获取失败！");
        }
    }

    /**
     * DELETE /oaFlowModels/:login : delete the "login" OaFlowModel.
     *
     * @param id the login of the oaFlowModel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiOperation(value = "删除__OaFlowModel__", notes = "根据wid 删除一个__OaFlowModel__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    @DeleteMapping("/oaFlowModels/{wid}")
    public ResponseEntity<Map> deleteOaFlowModel(@PathVariable Long id) {
        log.debug("REST request to delete OaFlowModel: {}", id);
        int result = oaFlowModelService.deleteByPrimaryKey(id);
        if(result == 1){
            return ResponseUtil.success("删除成功！");
        }else{
            return ResponseUtil.error("删除失败！");
        }
    }

}
