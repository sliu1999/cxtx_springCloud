package com.cxtx.user_manage.web.rest;


import com.cxtx.common.unit.ResponseUtil;
import com.github.pagehelper.PageInfo;

import com.cxtx.user_manage.domain.OaFlowModelElement;
import com.cxtx.user_manage.service.OaFlowModelElementService;

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

@Api(tags = "OaFlowModelElement")
@RestController
@RequestMapping("/api")
public class OaFlowModelElementResource {

    private final Logger log = LoggerFactory.getLogger(OaFlowModelElementResource.class);

    private static final String ENTITY_NAME = "oaFlowModelElement";

    @Autowired
    private OaFlowModelElementService oaFlowModelElementService;

    /**
     * POST  /oaFlowModelElements  : Creates a new oaFlowModelElement.
     *
     * @param oaFlowModelElement the oaFlowModelElement to create
     * @return the ResponseEntity with status 200
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "新增__OaFlowModelElement__", notes = "新增一个__OaFlowModelElement__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "oaFlowModelElement", value = "__OaFlowModelElement__", required = true, paramType = "body", dataType = "OaFlowModelElement")
    @PostMapping("/oaFlowModelElements")
    public ResponseEntity<Map> createOaFlowModelElement(@Valid @RequestBody OaFlowModelElement oaFlowModelElement) throws URISyntaxException {
        log.debug("REST request to save OaFlowModelElement : {}", oaFlowModelElement);
        try {
            int result = oaFlowModelElementService.insertSelective(oaFlowModelElement);
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
     * PUT  /oaFlowModelElements : Updates an existing OaFlowModelElement.
     *
     * @param oaFlowModelElement the oaFlowModelElement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated oaFlowModelElement,
     * or with status 500 (Internal Server Error) if the oaFlowModelElement couldn't be updated
     */
    @ApiOperation(value = "更新__OaFlowModelElement__", notes = "根据指定wid 更新一个__OaFlowModelElement__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "oaFlowModelElement", value = "__OaFlowModelElement__", required = true, paramType = "body", dataType = "OaFlowModelElement")
    @PutMapping("/oaFlowModelElements")
    public ResponseEntity<Map> updateOaFlowModelElement(@Valid @RequestBody OaFlowModelElement oaFlowModelElement) {
        log.debug("REST request to update OaFlowModelElement : {}", oaFlowModelElement);
        try {
            int result = oaFlowModelElementService.updateByPrimaryKeySelective(oaFlowModelElement);
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
     * GET  /oaFlowModelElements : get all oaFlowModelElements.
     *
     * @return the ResponseEntity with status 200 (OK) and with body all oaFlowModelElements
     */
    @GetMapping("/oaFlowModelElements")
    @ApiOperation(value = "分页获取__OaFlowModelElement__", notes = "分页获取__OaFlowModelElement__列表", response = ResponseUtil.Response.class)
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "pageNum", value = "分页参数：第几页", required = true, paramType = "query", dataType = "int"),
                    @ApiImplicitParam(name = "pageSize", value = "分页参数：每页数量", required = true, paramType = "query", dataType = "int")
            }
    )
    public ResponseEntity<Map> queryByPageOaFlowModelElements(@ApiIgnore @RequestParam Map params) {
        try {
            PageInfo<OaFlowModelElement> result = oaFlowModelElementService.queryByPage(params);
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
     * GET  /oaFlowModelElements/id : get  oaFlowModelElement by id.
     *
     * @return the ResponseEntity with status 200 (OK) and with body all oaFlowModelElements
     */
    @ApiOperation(value = "获取__OaFlowModelElement__", notes = "根据wid 获取一个__OaFlowModelElement__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    @GetMapping("/oaFlowModelElements/{id}")
    public ResponseEntity<Map> getOaFlowModelElement(@PathVariable Long id) {
        OaFlowModelElement result = oaFlowModelElementService.selectByPrimaryKey(id);
        if (result != null) {
            return ResponseUtil.success(result);
        } else {
            return ResponseUtil.error("获取失败！");
        }
    }

    /**
     * DELETE /oaFlowModelElements/:login : delete the "login" OaFlowModelElement.
     *
     * @param wid the login of the oaFlowModelElement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiOperation(value = "删除__OaFlowModelElement__", notes = "根据wid 删除一个__OaFlowModelElement__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    @DeleteMapping("/oaFlowModelElements/{id}")
    public ResponseEntity<Map> deleteOaFlowModelElement(@PathVariable Long id) {
        log.debug("REST request to delete OaFlowModelElement: {}", id);
        int result = oaFlowModelElementService.deleteByPrimaryKey(id);
        if(result == 1){
            return ResponseUtil.success("删除成功！");
        }else{
            return ResponseUtil.error("删除失败！");
        }
    }

}
