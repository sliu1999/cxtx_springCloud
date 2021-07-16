package com.cxtx.user_manage.web.rest;

import com.cxtx.common.unit.ResponseUtil;
import com.github.pagehelper.PageInfo;

import com.cxtx.user_manage.domain.OaFlowModelDetail;
import com.cxtx.user_manage.service.OaFlowModelDetailService;
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


@Api(tags = "OaFlowModelDetail")
@RestController
@RequestMapping("/api")
public class OaFlowModelDetailResource {

    private final Logger log = LoggerFactory.getLogger(OaFlowModelDetailResource.class);

    private static final String ENTITY_NAME = "oaFlowModelDetail";

    @Autowired
    private OaFlowModelDetailService oaFlowModelDetailService;


    @ApiOperation(value = "新增__OaFlowModelDetail__", notes = "新增一个__OaFlowModelDetail__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "oaFlowModelDetail", value = "__OaFlowModelDetail__", required = true, paramType = "body", dataType = "OaFlowModelDetail")
    @PostMapping("/oaFlowModelDetails")
    public ResponseEntity<Map> createOaFlowModelDetail(@Valid @RequestBody OaFlowModelDetail oaFlowModelDetail) throws URISyntaxException {
        log.debug("REST request to save OaFlowModelDetail : {}", oaFlowModelDetail);
        try {
            int result = oaFlowModelDetailService.insertSelective(oaFlowModelDetail);
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
     * PUT  /oaFlowModelDetails : Updates an existing OaFlowModelDetail.
     *
     * @param oaFlowModelDetail the oaFlowModelDetail to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated oaFlowModelDetail,
     * or with status 500 (Internal Server Error) if the oaFlowModelDetail couldn't be updated
     */
    @ApiOperation(value = "更新__OaFlowModelDetail__", notes = "根据指定wid 更新一个__OaFlowModelDetail__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "oaFlowModelDetail", value = "__OaFlowModelDetail__", required = true, paramType = "body", dataType = "OaFlowModelDetail")
    @PutMapping("/oaFlowModelDetails")
    public ResponseEntity<Map> updateOaFlowModelDetail(@Valid @RequestBody OaFlowModelDetail oaFlowModelDetail) {
        log.debug("REST request to update OaFlowModelDetail : {}", oaFlowModelDetail);
        try {
            int result = oaFlowModelDetailService.updateByPrimaryKeySelective(oaFlowModelDetail);
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
     * GET  /oaFlowModelDetails : get all oaFlowModelDetails.
     *
     * @return the ResponseEntity with status 200 (OK) and with body all oaFlowModelDetails
     */
    @GetMapping("/oaFlowModelDetails")
    @ApiOperation(value = "分页获取__OaFlowModelDetail__", notes = "分页获取__OaFlowModelDetail__列表", response = ResponseUtil.Response.class)
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "pageNum", value = "分页参数：第几页", required = true, paramType = "query", dataType = "int"),
                    @ApiImplicitParam(name = "pageSize", value = "分页参数：每页数量", required = true, paramType = "query", dataType = "int")
            }
    )
    public ResponseEntity<Map> queryByPageOaFlowModelDetails(@ApiIgnore @RequestParam Map params) {
        try {
            PageInfo<OaFlowModelDetail> result = oaFlowModelDetailService.queryByPage(params);
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
     * GET  /oaFlowModelDetails/id : get  oaFlowModelDetail by id.
     *
     * @return the ResponseEntity with status 200 (OK) and with body all oaFlowModelDetails
     */
    @ApiOperation(value = "获取__OaFlowModelDetail__", notes = "根据wid 获取一个__OaFlowModelDetail__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    @GetMapping("/oaFlowModelDetails/{id}")
    public ResponseEntity<Map> getOaFlowModelDetail(@PathVariable Long id) {
        OaFlowModelDetail result = oaFlowModelDetailService.selectByPrimaryKey(id);
        if (result != null) {
            return ResponseUtil.success(result);
        } else {
            return ResponseUtil.error("获取失败！");
        }
    }

    /**
     * DELETE /oaFlowModelDetails/:login : delete the "login" OaFlowModelDetail.
     *
     * @param id the login of the oaFlowModelDetail to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiOperation(value = "删除__OaFlowModelDetail__", notes = "根据wid 删除一个__OaFlowModelDetail__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    @DeleteMapping("/oaFlowModelDetails/{id}")
    public ResponseEntity<Map> deleteOaFlowModelDetail(@PathVariable Long id) {
        log.debug("REST request to delete OaFlowModelDetail: {}", id);
        int result = oaFlowModelDetailService.deleteByPrimaryKey(id);
        if(result == 1){
            return ResponseUtil.success("删除成功！");
        }else{
            return ResponseUtil.error("删除失败！");
        }
    }

}
