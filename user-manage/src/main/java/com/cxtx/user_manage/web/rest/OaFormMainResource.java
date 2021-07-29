package com.cxtx.user_manage.web.rest;


import com.cxtx.common.domain.JwtModel;
import com.cxtx.common.unit.HttpServletUtils;
import com.cxtx.common.unit.ResponseUtil;
import com.github.pagehelper.PageInfo;

import com.cxtx.user_manage.domain.OaFormMain;
import com.cxtx.user_manage.service.OaFormMainService;
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
import java.util.List;
import java.util.Map;


@Api(tags = "OaFormMain")
@RestController
@RequestMapping("/api")
public class OaFormMainResource {

    private final Logger log = LoggerFactory.getLogger(OaFormMainResource.class);

    private static final String ENTITY_NAME = "oaFormMain";

    @Autowired
    private OaFormMainService oaFormMainService;


    @ApiOperation(value = "流程权限管理--新增", notes = "流程权限管理--新增", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "oaFormMain", value = "__OaFormMain__", required = true, paramType = "body", dataType = "OaFormMain")
    @PostMapping("/oaFormMains")
    public ResponseEntity<Map> createOaFormMain(@Valid @RequestBody OaFormMain oaFormMain) throws URISyntaxException {
        log.debug("REST request to save OaFormMain : {}", oaFormMain);
        JwtModel curUser = HttpServletUtils.getUserInfo();
        try {
            oaFormMain.setCreateBy(Long.valueOf(curUser.getUserId()));
            oaFormMain.setUpdateBy(Long.valueOf(curUser.getUserId()));
            int result = oaFormMainService.insertSelective(oaFormMain);
            if (result == 1) {
                return ResponseUtil.success("");
            } else {
                return ResponseUtil.error("新增失败！");
            }
        }catch (Exception e){
        return ResponseUtil.error(e.getMessage());
        }
    }


    @ApiOperation(value = "流程权限管理--编辑", notes = "流程权限管理--编辑", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "oaFormMain", value = "__OaFormMain__", required = true, paramType = "body", dataType = "OaFormMain")
    @PutMapping("/oaFormMains")
    public ResponseEntity<Map> updateOaFormMain(@Valid @RequestBody OaFormMain oaFormMain) {
        log.debug("REST request to update OaFormMain : {}", oaFormMain);
        JwtModel curUser = HttpServletUtils.getUserInfo();
        try {
            oaFormMain.setUpdateBy(Long.valueOf(curUser.getUserId()));
            int result = oaFormMainService.updateByPrimaryKeySelective(oaFormMain);
            if (result == 1) {
                return ResponseUtil.success("");
            } else {
                return ResponseUtil.error("更新失败");
            }
        }catch (Exception e){
            return ResponseUtil.error(e.getMessage());
        }
    }


    @GetMapping("/oaFormMains")
    @ApiOperation(value = "分页获取__OaFormMain__", notes = "分页获取__OaFormMain__列表", response = ResponseUtil.Response.class)
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "pageNum", value = "分页参数：第几页", required = true, paramType = "query", dataType = "int"),
                    @ApiImplicitParam(name = "pageSize", value = "分页参数：每页数量", required = true, paramType = "query", dataType = "int")
            }
    )
    public ResponseEntity<Map> queryByPageOaFormMains(@ApiIgnore @RequestParam Map params) {
        try {
            PageInfo<OaFormMain> result = oaFormMainService.queryByPage(params);
            if (result != null) {
                return ResponseUtil.success(result);
            } else {
                return ResponseUtil.error("有错误");
            }
        }catch (Exception e){
            return ResponseUtil.error(e.getMessage());
        }
    }

    @GetMapping("/oaFormMains/list")
    @ApiOperation(value = "流程权限管理--发起权限列表", notes = "流程权限管理--发起权限列表", response = ResponseUtil.Response.class)
    public ResponseEntity<Map> list() {
        try {
            List<OaFormMain> result = oaFormMainService.list();
            if (result != null) {
                return ResponseUtil.success(result);
            } else {
                return ResponseUtil.error("有错误");
            }
        }catch (Exception e){
            return ResponseUtil.error(e.getMessage());
        }
    }



    @ApiOperation(value = "流程权限管理--获取流程权限详情", notes = "流程权限管理--获取流程权限详情", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    @GetMapping("/oaFormMains/{id}")
    public ResponseEntity<Map> getOaFormMain(@PathVariable Long id) {
        OaFormMain result = oaFormMainService.selectByPrimaryKey(id);
        if (result != null) {
            return ResponseUtil.success(result);
        } else {
            return ResponseUtil.error("获取失败！");
        }
    }


    @ApiOperation(value = "流程权限管理--删除", notes = "流程权限管理--删除", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    @DeleteMapping("/oaFormMains/{id}")
    public ResponseEntity<Map> deleteOaFormMain(@PathVariable Long id) {
        log.debug("REST request to delete OaFormMain: {}", id);
        try {
            int result = oaFormMainService.deleteByPrimaryKey(id);
            if(result == 1){
                return ResponseUtil.success("删除成功！");
            }else{
                return ResponseUtil.error("删除失败！");
            }
        }catch (Exception e){
            return ResponseUtil.error(e.getMessage());
        }

    }

}
