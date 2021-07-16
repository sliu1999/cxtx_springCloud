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
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.Map;

@Api(tags = "OaFlow")
@RestController
@RequestMapping("/api")
public class OaFlowResource {

    private final Logger log = LoggerFactory.getLogger(OaFlowResource.class);

    private static final String ENTITY_NAME = "oaFlow";

    @Autowired
    private OaFlowService oaFlowService;

    /**
     * POST  /oaFlows  : Creates a new oaFlow.
     *
     * @param oaFlow the oaFlow to create
     * @return the ResponseEntity with status 200
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "新增__OaFlow__", notes = "新增一个__OaFlow__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "oaFlow", value = "__OaFlow__", required = true, paramType = "body", dataType = "OaFlow")
    @PostMapping("/oaFlows")
    public ResponseEntity<Map> createOaFlow(@Valid @RequestBody OaFlow oaFlow) throws URISyntaxException {
        log.debug("REST request to save OaFlow : {}", oaFlow);
        try {
            int result = oaFlowService.insertSelective(oaFlow);
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
     * PUT  /oaFlows : Updates an existing OaFlow.
     *
     * @param oaFlow the oaFlow to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated oaFlow,
     * or with status 500 (Internal Server Error) if the oaFlow couldn't be updated
     */
    @ApiOperation(value = "更新__OaFlow__", notes = "根据指定wid 更新一个__OaFlow__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "oaFlow", value = "__OaFlow__", required = true, paramType = "body", dataType = "OaFlow")
    @PutMapping("/oaFlows")
    public ResponseEntity<Map> updateOaFlow(@Valid @RequestBody OaFlow oaFlow) {
        log.debug("REST request to update OaFlow : {}", oaFlow);
        try {
            int result = oaFlowService.updateByPrimaryKeySelective(oaFlow);
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
     * GET  /oaFlows : get all oaFlows.
     *
     * @return the ResponseEntity with status 200 (OK) and with body all oaFlows
     */
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


    /**
     * GET  /oaFlows/id : get  oaFlow by id.
     *
     * @return the ResponseEntity with status 200 (OK) and with body all oaFlows
     */
    @ApiOperation(value = "获取__OaFlow__", notes = "根据wid 获取一个__OaFlow__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    @GetMapping("/oaFlows/{wid}")
    public ResponseEntity<Map> getOaFlow(@PathVariable Long id) {
        OaFlow result = oaFlowService.selectByPrimaryKey(id);
        if (result != null) {
            return ResponseUtil.success(result);
        } else {
            return ResponseUtil.error("获取失败！");
        }
    }

    /**
     * DELETE /oaFlows/:login : delete the "login" OaFlow.
     *
     * @param id the login of the oaFlow to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiOperation(value = "删除__OaFlow__", notes = "根据wid 删除一个__OaFlow__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    @DeleteMapping("/oaFlows/{id}")
    public ResponseEntity<Map> deleteOaFlow(@PathVariable Long id) {
        log.debug("REST request to delete OaFlow: {}", id);
        int result = oaFlowService.deleteByPrimaryKey(id);
        if(result == 1){
            return ResponseUtil.success("删除成功！");
        }else{
            return ResponseUtil.error("删除失败！");
        }
    }

}
