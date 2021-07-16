package com.cxtx.user_manage.web.rest;

import com.cxtx.common.unit.ResponseUtil;
import com.github.pagehelper.PageInfo;

import com.cxtx.user_manage.domain.OaFlowModelElementConfig;
import com.cxtx.user_manage.service.OaFlowModelElementConfigService;
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


@Api(tags = "OaFlowModelElementConfig")
@RestController
@RequestMapping("/api")
public class OaFlowModelElementConfigResource {

    private final Logger log = LoggerFactory.getLogger(OaFlowModelElementConfigResource.class);

    private static final String ENTITY_NAME = "oaFlowModelElementConfig";

    @Autowired
    private OaFlowModelElementConfigService oaFlowModelElementConfigService;

    /**
     * POST  /oaFlowModelElementConfigs  : Creates a new oaFlowModelElementConfig.
     *
     * @param oaFlowModelElementConfig the oaFlowModelElementConfig to create
     * @return the ResponseEntity with status 200
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "新增__OaFlowModelElementConfig__", notes = "新增一个__OaFlowModelElementConfig__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "oaFlowModelElementConfig", value = "__OaFlowModelElementConfig__", required = true, paramType = "body", dataType = "OaFlowModelElementConfig")
    @PostMapping("/oaFlowModelElementConfigs")
    public ResponseEntity<Map> createOaFlowModelElementConfig(@Valid @RequestBody OaFlowModelElementConfig oaFlowModelElementConfig) throws URISyntaxException {
        log.debug("REST request to save OaFlowModelElementConfig : {}", oaFlowModelElementConfig);
        try {
            int result = oaFlowModelElementConfigService.insertSelective(oaFlowModelElementConfig);
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
     * PUT  /oaFlowModelElementConfigs : Updates an existing OaFlowModelElementConfig.
     *
     * @param oaFlowModelElementConfig the oaFlowModelElementConfig to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated oaFlowModelElementConfig,
     * or with status 500 (Internal Server Error) if the oaFlowModelElementConfig couldn't be updated
     */
    @ApiOperation(value = "更新__OaFlowModelElementConfig__", notes = "根据指定wid 更新一个__OaFlowModelElementConfig__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "oaFlowModelElementConfig", value = "__OaFlowModelElementConfig__", required = true, paramType = "body", dataType = "OaFlowModelElementConfig")
    @PutMapping("/oaFlowModelElementConfigs")
    public ResponseEntity<Map> updateOaFlowModelElementConfig(@Valid @RequestBody OaFlowModelElementConfig oaFlowModelElementConfig) {
        log.debug("REST request to update OaFlowModelElementConfig : {}", oaFlowModelElementConfig);
        try {
            int result = oaFlowModelElementConfigService.updateByPrimaryKeySelective(oaFlowModelElementConfig);
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
     * GET  /oaFlowModelElementConfigs : get all oaFlowModelElementConfigs.
     *
     * @return the ResponseEntity with status 200 (OK) and with body all oaFlowModelElementConfigs
     */
    @GetMapping("/oaFlowModelElementConfigs")
    @ApiOperation(value = "分页获取__OaFlowModelElementConfig__", notes = "分页获取__OaFlowModelElementConfig__列表", response = ResponseUtil.Response.class)
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "pageNum", value = "分页参数：第几页", required = true, paramType = "query", dataType = "int"),
                    @ApiImplicitParam(name = "pageSize", value = "分页参数：每页数量", required = true, paramType = "query", dataType = "int")
            }
    )
    public ResponseEntity<Map> queryByPageOaFlowModelElementConfigs(@ApiIgnore @RequestParam Map params) {
        try {
            PageInfo<OaFlowModelElementConfig> result = oaFlowModelElementConfigService.queryByPage(params);
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
     * GET  /oaFlowModelElementConfigs/id : get  oaFlowModelElementConfig by id.
     *
     * @return the ResponseEntity with status 200 (OK) and with body all oaFlowModelElementConfigs
     */
    @ApiOperation(value = "获取__OaFlowModelElementConfig__", notes = "根据wid 获取一个__OaFlowModelElementConfig__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    @GetMapping("/oaFlowModelElementConfigs/{id}")
    public ResponseEntity<Map> getOaFlowModelElementConfig(@PathVariable Long id) {
        OaFlowModelElementConfig result = oaFlowModelElementConfigService.selectByPrimaryKey(id);
        if (result != null) {
            return ResponseUtil.success(result);
        } else {
            return ResponseUtil.error("获取失败！");
        }
    }

    /**
     * DELETE /oaFlowModelElementConfigs/:login : delete the "login" OaFlowModelElementConfig.
     *
     * @param id the login of the oaFlowModelElementConfig to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiOperation(value = "删除__OaFlowModelElementConfig__", notes = "根据wid 删除一个__OaFlowModelElementConfig__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    @DeleteMapping("/oaFlowModelElementConfigs/{wid}")
    public ResponseEntity<Map> deleteOaFlowModelElementConfig(@PathVariable Long id) {
        log.debug("REST request to delete OaFlowModelElementConfig: {}", id);
        int result = oaFlowModelElementConfigService.deleteByPrimaryKey(id);
        if(result == 1){
            return ResponseUtil.success("删除成功！");
        }else{
            return ResponseUtil.error("删除失败！");
        }
    }

}
