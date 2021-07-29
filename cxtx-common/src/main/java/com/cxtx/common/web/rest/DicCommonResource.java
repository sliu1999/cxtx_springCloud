package com.cxtx.common.web.rest;

import com.cxtx.common.domain.DicCommon;
import com.cxtx.common.service.DicCommonService;
import com.cxtx.common.service.FileInfoService;
import com.cxtx.common.unit.ResponseUtil;
import com.cxtx.common.web.rest.vo.DicSearchVO;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Api(
        tags = {"字典表管理"},
        description = "字典表数据查询"
)
@RestController
@RequestMapping({"/api"})
public class DicCommonResource {
    private final Logger log = LoggerFactory.getLogger(DicCommonResource.class);
    @Autowired
    private DicCommonService dicCommonService;


    @ApiOperation(
            value = "字典表查询",
            notes = "根据表名查询字典表",
            code = 200,
            produces = "application/json",
            response = ResponseUtil.Response.class
    )
    @ApiImplicitParams({@ApiImplicitParam(
            name = "tableName",
            value = "字典表 表名",
            required = true,
            dataType = "string",
            paramType = "query"
    ), @ApiImplicitParam(
            name = "orderBy",
            value = "排序字段 字段名",
            required = false,
            dataType = "string",
            paramType = "query"
    )})
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "{\"msg\":\"\",\"data\":{\"total\":3,\"content\":[{\"name\":\"A\",\"id\":\"1\"},{\"name\":\"B\",\"id\":\"2\"},{\"name\":\"C\",\"id\":\"3\"}]},\"errorCode\":0}"
    )})
    @RequestMapping(
            value = {"/selectCommonDic"},
            method = {RequestMethod.GET}
    )
    public ResponseEntity<?> selectDic(@ApiIgnore @RequestParam HashMap<String, Object> params) {
        try {
            String contentKey = "content";
            HashMap<String, Object> hm = this.dicCommonService.selectAll(params);
            return hm.get(contentKey) != null ? ResponseUtil.success(hm) : ResponseUtil.error("缺少参数");
        } catch (Exception var4) {
            return ResponseUtil.error(var4.getMessage());
        }
    }

    @ApiOperation(
            value = "字典表批量查询",
            notes = "根据表名查询字典表",
            code = 200,
            produces = "application/json",
            response = ResponseUtil.Response.class
    )
    @RequestMapping(
            value = {"/selectCommonDic/multi"},
            method = {RequestMethod.POST}
    )
    public ResponseEntity<?> selectDics(@RequestBody List<DicSearchVO> dicSearchVOs) {
        try {
            HashMap<String, Object> result = new HashMap(16);
            Iterator var3 = dicSearchVOs.iterator();

            while(var3.hasNext()) {
                DicSearchVO dicSearchVO = (DicSearchVO)var3.next();
                HashMap<String, Object> params = new HashMap(16);
                params.put("tableName", dicSearchVO.getTableName());
                params.put("orderBy", dicSearchVO.getOrderBy());
                HashMap<String, Object> dicMap = this.dicCommonService.selectAll(params);
                result.put(dicSearchVO.getObjName(), dicMap);
            }

            return result.size() > 0 ? ResponseUtil.success(result) : ResponseUtil.error("缺少参数");
        } catch (Exception var7) {
            return ResponseUtil.error(var7.getMessage());
        }
    }

    @RequestMapping(
            value = {"/selectOneDic"},
            method = {RequestMethod.GET}
    )
    @ApiOperation(
            value = "单个详情",
            notes = "单个详情",
            code = 200,
            produces = "application/json",
            response = ResponseUtil.Response.class
    )
    public ResponseEntity<?> selectOneDic(@ApiIgnore @RequestParam HashMap<String, Object> params) {
        try {
            DicCommon hm = this.dicCommonService.selectOneDic(params);
            return hm != null ? ResponseUtil.success(hm) : ResponseUtil.error("缺少参数");
        } catch (Exception var4) {
            return ResponseUtil.error(var4.getMessage());
        }
    }
}

