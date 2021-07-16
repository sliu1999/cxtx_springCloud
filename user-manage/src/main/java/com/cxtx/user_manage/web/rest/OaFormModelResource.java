package com.cxtx.user_manage.web.rest;


import com.cxtx.common.unit.ResponseUtil;
import com.cxtx.user_manage.service.AppService;
import com.github.pagehelper.PageInfo;

import com.cxtx.user_manage.domain.OaFormModel;
import com.cxtx.user_manage.service.OaFormModelService;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Api(tags = "OaFormModel")
@RestController
@RequestMapping("/api")
public class OaFormModelResource {

    private final Logger log = LoggerFactory.getLogger(OaFormModelResource.class);

    private static final String ENTITY_NAME = "oaFormModel";

    @Autowired
    private OaFormModelService oaFormModelService;

    @Autowired
    private AppService appService;


    @ApiOperation(value = "新增__OaFormModel__", notes = "新增一个__OaFormModel__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "oaFormModel", value = "__OaFormModel__", required = true, paramType = "body", dataType = "OaFormModel")
    @PostMapping("/oaFormModels")
    public ResponseEntity<Map> createOaFormModel(@Valid @RequestBody OaFormModel oaFormModel) throws URISyntaxException {
        log.debug("REST request to save OaFormModel : {}", oaFormModel);
        try {


            //和正在使用中的表单名称是否存在冲突
            HashMap param = new HashMap(2);
            param.put("name",oaFormModel.getName());
            param.put("status","1");
            List<OaFormModel> withNameNum = oaFormModelService.selectAll(param);
            if(withNameNum != null && withNameNum.size()>0){
                return ResponseUtil.error("该表单名称已存在！！！");
            }

            //和正在使用中的表单名称是否存在冲突
            HashMap param2 = new HashMap(2);
            param2.put("name",oaFormModel.getName());
            param2.put("status","0");
            List<OaFormModel> withNameNumTwo = oaFormModelService.selectAll(param2);
            if(withNameNumTwo != null && withNameNumTwo.size()==1){
                //如果存在名称一样但没启用的，则修改老的名称
                OaFormModel temp = withNameNumTwo.get(0);
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String oldFormName = oaFormModel.getName() + "_" + format.format(date);
                temp.setName(oldFormName);
                oaFormModelService.updateByPrimaryKeySelective(temp);
            }

            // 将前端输入的tableKey和数据库中的进行比对，如果存在重复则进行后缀数字，直到不重复为止
            String tableKey = appService.updateTableKey(oaFormModel.getTableKey());


            Map tableKeyMap = new HashMap(1);
            tableKeyMap.put("tableKey", tableKey);
            appService.createTable(tableKeyMap);

            oaFormModel.setTableKey(tableKey);
            int result = oaFormModelService.insertSelective(oaFormModel);
            if (result == 1) {
//                oaFormMod = oaFormModService.getOne(new QueryWrapper<OaFormMod>().eq("table_key", tableKey));
//                // 添加表单流程
//                OaFormModFlow modFlow = new OaFormModFlow();
//                modFlow.setTenantId(getCurrentUser().getTenantId());
//                modFlow.setModId(oaFormMod.getId());
//                modFlow.setFlowConfig(
//                        "{\"global\":{},\"nodesMap\":{\"start\":{\"id\":\"start\",\"name\":\"开始节点\",\"flagStart\":true,\"x\":0.1,\"y\":0.5,"
//                                + "\"handlers\":[{\"type\":0,\"flagSelfHandler\":true}],\"nextNodes\":[{\"id\":\"end\",\"condition\":\"true\",\"name\":\"默认通过\"}]},"
//                                + "\"end\":{\"id\":\"end\",\"name\":\"结束节点\",\"flagEnd\":true,\"x\":0.8,\"y\":0.8,\"prevNodes\":[{\"id\":\"start\",\"condition\":\"true\",\"name\":\"默认返回\"}]}}}");
//                boolean flag = oaFormModFlowService.save(modFlow);
//                System.out.println(flag ? "成功添加formConfig！" : "添加失败！");
//                return Result.ok(oaFormMod);
                return ResponseUtil.success("");
            } else {
                return ResponseUtil.error("新增失败！");
            }
        }catch (Exception e){
        return ResponseUtil.error(e.getMessage());
        }
    }


    @ApiOperation(value = "更新__OaFormModel__", notes = "根据指定id 更新一个__OaFormModel__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "oaFormModel", value = "__OaFormModel__", required = true, paramType = "body", dataType = "OaFormModel")
    @PutMapping("/oaFormModels")
    public ResponseEntity<Map> updateOaFormModel(@Valid @RequestBody OaFormModel oaFormModel) {
        log.debug("REST request to update OaFormModel : {}", oaFormModel);
        try {
            int result = oaFormModelService.updateByPrimaryKeySelective(oaFormModel);
            if (result == 1) {
                return ResponseUtil.success("");
            } else {
                return ResponseUtil.error("更新失败");
            }
        }catch (Exception e){
            return ResponseUtil.error(e.getMessage());
        }
    }


    @GetMapping("/oaFormModels")
    @ApiOperation(value = "分页获取__OaFormModel__", notes = "分页获取__OaFormModel__列表", response = ResponseUtil.Response.class)
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "pageNum", value = "分页参数：第几页", required = true, paramType = "query", dataType = "int"),
                    @ApiImplicitParam(name = "pageSize", value = "分页参数：每页数量", required = true, paramType = "query", dataType = "int")
            }
    )
    public ResponseEntity<Map> queryByPageOaFormModels(@ApiIgnore @RequestParam Map params) {
        try {
            PageInfo<OaFormModel> result = oaFormModelService.queryByPage(params);
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
     * 对表单进行增删改
     *
     * @param dataMap
     * @return
     */
    @PostMapping("/updateFormModelConfig")
    public ResponseEntity<Map> updateFormModelConfig(@RequestBody Map<String, Object> dataMap) {
        Map<String, Object> result = new HashMap<String, Object>();
        boolean flag = true;
        String message="";
        if (dataMap.keySet().size() > 0) {
            Long modId = Long.valueOf(dataMap.get("id").toString());
            //获取表单详情
            OaFormModel modEntity = oaFormModelService.selectByPrimaryKey(modId);
            String tableName = "_app_" + modEntity.getTableKey();
            try {
                // 对主表进行操作
                flag = appService.operateTable(tableName, dataMap);
                // 对明细表进行操作
                if (flag) {
                    flag = appService.operateDetailTable(tableName, dataMap);
                }
                // 表操作完成之后对sys_mod表进行操作
                if (flag) {
                    modEntity.setId(modId);
                    modEntity.setDetailKeys((String) dataMap.get("detailKeys"));
                    modEntity.setFormView((String) dataMap.get("formView"));
                    modEntity.setTableSchema((String) dataMap.get("tableSchema"));
                    if (oaFormModelService.updateByPrimaryKeySelective(modEntity)!=1) {
                        flag = false;
                    }
                }
            } catch (Exception e) {
                message = e.getMessage();
                flag = false;
            }
        }
        result.put("code", flag ? 10000 : 20000);
        result.put("message", flag ? "请求成功" : message);
        result.put("status", flag ? "SUCCESS" : "ERROR");
        return ResponseUtil.success(result);
    }



    /**
     * GET  /oaFormModels/id : get  oaFormModel by id.
     *
     * @return the ResponseEntity with status 200 (OK) and with body all oaFormModels
     */
    @ApiOperation(value = "获取__OaFormModel__", notes = "根据id 获取一个__OaFormModel__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    @GetMapping("/oaFormModels/{id}")
    public ResponseEntity<Map> getOaFormModel(@PathVariable Long id) {
        OaFormModel formMod = oaFormModelService.selectByPrimaryKey(id);
        Map<String,Object> startNodeConfig = oaFormModelService.getStartElementConfigByFormModId(id);
        formMod.setStartNodeConfig(startNodeConfig);
        return ResponseUtil.success(formMod);

    }

    /**
     * DELETE /oaFormModels/:login : delete the "login" OaFormModel.
     *
     * @param id the login of the oaFormModel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiOperation(value = "删除__OaFormModel__", notes = "根据id 删除一个__OaFormModel__", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    @DeleteMapping("/oaFormModels/{id}")
    public ResponseEntity<Map> deleteOaFormModel(@PathVariable Long id) {
        log.debug("REST request to delete OaFormModel: {}", id);
        int result = oaFormModelService.deleteByPrimaryKey(id);
        if(result == 1){
            return ResponseUtil.success("删除成功！");
        }else{
            return ResponseUtil.error("删除失败！");
        }
    }

}
