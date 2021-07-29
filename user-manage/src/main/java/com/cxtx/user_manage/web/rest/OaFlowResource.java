package com.cxtx.user_manage.web.rest;

import com.cxtx.common.unit.ResponseUtil;
import com.github.pagehelper.PageInfo;

import com.cxtx.user_manage.domain.OaFlow;
import com.cxtx.user_manage.service.OaFlowService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@Api(tags = "OaFlow")
@RestController
@RequestMapping("/api")
public class OaFlowResource {

    private final Logger log = LoggerFactory.getLogger(OaFlowResource.class);

    private static final String ENTITY_NAME = "oaFlow";

    @Autowired
    private OaFlowService oaFlowService;


    @PostMapping("/addOrEditFlow")
    @ApiOperation(value = "流程设计--增加或编辑流程", notes = "流程设计--增加或编辑流程")
    public ResponseEntity<Map> addOrEditFlow(@Valid @RequestBody OaFlow oaFlow){
        try {
            Map result = oaFlowService.addOrEditFlow(oaFlow);
            return ResponseUtil.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.error(20000,e.getMessage());
        }
    }

    @ApiOperation(value = "流程设计--根据流程ID获取流程详情", notes = "流程设计--根据流程ID获取流程详情", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    @GetMapping("/oaFlows/{id}")
    public ResponseEntity<Map> getOaFlow(@PathVariable Long id) {
        OaFlow result = oaFlowService.selectByPrimaryKey(id);
        if (result != null) {
            return ResponseUtil.success(result);
        } else {
            return ResponseUtil.error("获取失败！");
        }
    }


    @GetMapping("/oaFlows")
    @ApiOperation(value = "分页获取__OaFlow__", notes = "分页获取__OaFlow__列表", response = ResponseUtil.Response.class)
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "pageNum", value = "分页参数：第几页", required = true, paramType = "query", dataType = "int"),
                    @ApiImplicitParam(name = "pageSize", value = "分页参数：每页数量", required = true, paramType = "query", dataType = "int")
            }
    )
    public ResponseEntity<Map> queryByPageOaFlows(@ApiIgnore @RequestParam Map params) {
        try {
            PageInfo<OaFlow> result = oaFlowService.queryByPage(params);
            if (result != null) {
                return ResponseUtil.success(result);
            } else {
                return ResponseUtil.error("有错误");
            }
        }catch (Exception e){
            return ResponseUtil.error(e.getMessage());
        }
    }

    @GetMapping("/oaFlows/list")
    @ApiOperation(value = "获取所有有效流程", notes = "获取所有有效流程", response = ResponseUtil.Response.class)
    public ResponseEntity<Map> list(@ApiIgnore @RequestParam Map params) {
        try {
            params.put("status","1");
            List<OaFlow> result = oaFlowService.selectAll(params);
            if (result != null) {
                return ResponseUtil.success(result);
            } else {
                return ResponseUtil.error("有错误");
            }
        }catch (Exception e){
            return ResponseUtil.error(e.getMessage());
        }
    }






    @ApiOperation(value = "流程设计--删除流程", notes = "流程设计--删除流程", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    @DeleteMapping("/oaFlows/{id}")
    public ResponseEntity<Map> deleteOaFlow(@PathVariable Long id) {
        log.debug("REST request to delete OaFlow: {}", id);
        try {
            int result = oaFlowService.deleteByPrimaryKey(id);
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
