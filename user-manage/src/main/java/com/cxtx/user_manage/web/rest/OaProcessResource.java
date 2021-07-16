package com.cxtx.user_manage.web.rest;

import com.cxtx.common.unit.ResponseUtil;
import com.github.pagehelper.PageInfo;

import com.cxtx.user_manage.domain.OaProcess;
import com.cxtx.user_manage.service.OaProcessService;
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


@Api(tags = "OaProcess")
@RestController
@RequestMapping("/api")
public class OaProcessResource {

    private final Logger log = LoggerFactory.getLogger(OaProcessResource.class);

    private static final String ENTITY_NAME = "oaProcess";

    @Autowired
    private OaProcessService oaProcessService;

    /**
     * POST  /oaProcesss  : Creates a new oaProcess.
     *
     * @param oaProcess the oaProcess to create
     * @return the ResponseEntity with status 200
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "新增__OaProcess__", notes = "新增一个__OaProcess__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "oaProcess", value = "__OaProcess__", required = true, paramType = "body", dataType = "OaProcess")
    @PostMapping("/oaProcess")
    public ResponseEntity<Map> createOaProcess(@Valid @RequestBody OaProcess oaProcess) throws URISyntaxException {
        log.debug("REST request to save OaProcess : {}", oaProcess);
        try {
            int result = oaProcessService.insertSelective(oaProcess);
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
     * PUT  /oaProcesss : Updates an existing OaProcess.
     *
     * @param oaProcess the oaProcess to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated oaProcess,
     * or with status 500 (Internal Server Error) if the oaProcess couldn't be updated
     */
    @ApiOperation(value = "更新__OaProcess__", notes = "根据指定id 更新一个__OaProcess__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "oaProcess", value = "__OaProcess__", required = true, paramType = "body", dataType = "OaProcess")
    @PutMapping("/oaProcess")
    public ResponseEntity<Map> updateOaProcess(@Valid @RequestBody OaProcess oaProcess) {
        log.debug("REST request to update OaProcess : {}", oaProcess);
        try {
            int result = oaProcessService.updateByPrimaryKeySelective(oaProcess);
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
     * GET  /oaProcesss : get all oaProcesss.
     *
     * @return the ResponseEntity with status 200 (OK) and with body all oaProcesss
     */
    @GetMapping("/oaProcess")
    @ApiOperation(value = "分页获取__OaProcess__", notes = "分页获取__OaProcess__列表", response = ResponseUtil.Response.class)
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "pageNum", value = "分页参数：第几页", required = true, paramType = "query", dataType = "int"),
                    @ApiImplicitParam(name = "pageSize", value = "分页参数：每页数量", required = true, paramType = "query", dataType = "int")
            }
    )
    public ResponseEntity<Map> queryByPageOaProcesss(@ApiIgnore @RequestParam Map params) {
        try {
            PageInfo<OaProcess> result = oaProcessService.queryByPage(params);
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
     * DELETE /oaProcesss/:login : delete the "login" OaProcess.
     *
     * @param id the login of the oaProcess to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiOperation(value = "删除__OaProcess__", notes = "根据id 删除一个__OaProcess__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    @DeleteMapping("/oaProcess/{id}")

    public ResponseEntity<Map> deleteOaProcess(@PathVariable Long id) {
        log.debug("REST request to delete OaProcess: {}", id);
        int result = oaProcessService.deleteByPrimaryKey(id);
        if(result == 1){
            return ResponseUtil.success("删除成功！");
        }else{
            return ResponseUtil.error("删除失败！");
        }
    }

}
