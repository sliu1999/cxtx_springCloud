package com.cxtx.user_manage.web.rest;

import com.cxtx.common.unit.ResponseUtil;
import com.github.pagehelper.PageInfo;

import com.cxtx.user_manage.domain.OaFormAdd;
import com.cxtx.user_manage.service.OaFormAddService;
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


@Api(tags = "OaFormAdd")
@RestController
@RequestMapping("/api")
public class OaFormAddResource {

    private final Logger log = LoggerFactory.getLogger(OaFormAddResource.class);

    private static final String ENTITY_NAME = "oaFormAdd";

    @Autowired
    private OaFormAddService oaFormAddService;


    @ApiOperation(value = "新增__OaFormAdd__", notes = "新增一个__OaFormAdd__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "oaFormAdd", value = "__OaFormAdd__", required = true, paramType = "body", dataType = "OaFormAdd")
    @PostMapping("/oaFormAdds")
    public ResponseEntity<Map> createOaFormAdd(@Valid @RequestBody OaFormAdd oaFormAdd) throws URISyntaxException {
        log.debug("REST request to save OaFormAdd : {}", oaFormAdd);
        try {
            int result = oaFormAddService.insertSelective(oaFormAdd);
            if (result == 1) {
                return ResponseUtil.success("");
            } else {
                return ResponseUtil.error("新增失败！");
            }
        }catch (Exception e){
        return ResponseUtil.error(e.getMessage());
        }
    }


    @ApiOperation(value = "更新__OaFormAdd__", notes = "根据指定wid 更新一个__OaFormAdd__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "oaFormAdd", value = "__OaFormAdd__", required = true, paramType = "body", dataType = "OaFormAdd")
    @PutMapping("/oaFormAdds")
    public ResponseEntity<Map> updateOaFormAdd(@Valid @RequestBody OaFormAdd oaFormAdd) {
        log.debug("REST request to update OaFormAdd : {}", oaFormAdd);
        try {
            int result = oaFormAddService.updateByPrimaryKeySelective(oaFormAdd);
            if (result == 1) {
                return ResponseUtil.success("");
            } else {
                return ResponseUtil.error("更新失败");
            }
        }catch (Exception e){
            return ResponseUtil.error(e.getMessage());
        }
    }


    @GetMapping("/oaFormAdds")
    @ApiOperation(value = "分页获取__OaFormAdd__", notes = "分页获取__OaFormAdd__列表", response = ResponseUtil.Response.class)
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "pageNum", value = "分页参数：第几页", required = true, paramType = "query", dataType = "int"),
                    @ApiImplicitParam(name = "pageSize", value = "分页参数：每页数量", required = true, paramType = "query", dataType = "int")
            }
    )
    public ResponseEntity<Map> queryByPageOaFormAdds(@ApiIgnore @RequestParam Map params) {
        try {
            PageInfo<OaFormAdd> result = oaFormAddService.queryByPage(params);
            if (result != null) {
                return ResponseUtil.success(result);
            } else {
                return ResponseUtil.error("有错误");
            }
        }catch (Exception e){
            return ResponseUtil.error(e.getMessage());
        }
    }



    @ApiOperation(value = "获取__OaFormAdd__", notes = "根据wid 获取一个__OaFormAdd__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    @GetMapping("/oaFormAdds/{id}")
    public ResponseEntity<Map> getOaFormAdd(@PathVariable Long id) {
        OaFormAdd result = oaFormAddService.selectByPrimaryKey(id);
        if (result != null) {
            return ResponseUtil.success(result);
        } else {
            return ResponseUtil.error("获取失败！");
        }
    }


    @ApiOperation(value = "删除__OaFormAdd__", notes = "根据wid 删除一个__OaFormAdd__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    @DeleteMapping("/oaFormAdds/{id}")
    public ResponseEntity<Map> deleteOaFormAdd(@PathVariable Long id) {
        log.debug("REST request to delete OaFormAdd: {}", id);
        int result = oaFormAddService.deleteByPrimaryKey(id);
        if(result == 1){
            return ResponseUtil.success("删除成功！");
        }else{
            return ResponseUtil.error("删除失败！");
        }
    }

}
