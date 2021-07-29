package com.cxtx.user_manage.web.rest;


import com.cxtx.common.domain.DicCommon;
import com.cxtx.common.domain.JwtModel;
import com.cxtx.common.service.DicCommonService;
import com.cxtx.common.unit.HttpServletUtils;
import com.cxtx.common.unit.ResponseUtil;
import com.cxtx.user_manage.domain.OaFlow;
import com.cxtx.user_manage.service.AppService;
import com.cxtx.user_manage.service.OaFlowService;
import com.cxtx.user_manage.unit.GuavaUtil;
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
import java.util.*;


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

    @Autowired
    private DicCommonService dicCommonService;

    @Autowired
    private OaFlowService oaFlowService;


    @ApiOperation(value = "表单设计--新增表单", notes = "表单设计--新增表单", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "oaFormModel", value = "__OaFormModel__", required = true, paramType = "body", dataType = "OaFormModel")
    @PostMapping("/oaFormModels")
    public ResponseEntity<Map> createOaFormModel(@Valid @RequestBody OaFormModel oaFormModel) throws URISyntaxException {
        log.debug("REST request to save OaFormModel : {}", oaFormModel);
        try {
            JwtModel curUser = HttpServletUtils.getUserInfo();

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
            oaFormModel.setCreateBy(Long.valueOf(curUser.getUserId()));
            oaFormModel.setUpdateBy(Long.valueOf(curUser.getUserId()));
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


    @ApiOperation(value = "表单管理--编辑基本信息（名称和分类）", notes = "表单管理--编辑基本信息（名称和分类）", response = ResponseUtil.Response.class)
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


    @PostMapping("/updateFormModelConfig")
    @ApiOperation(value = "表单设计--修改表单模型", response = ResponseUtil.Response.class)
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




    @ApiOperation(value = "表单设计--获取表单详情", notes = "表单设计--获取表单详情", response = ResponseUtil.Response.class)
    @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "path")
    @GetMapping("/oaFormModels/{id}")
    public ResponseEntity<Map> getOaFormModel(@PathVariable Long id) {
        OaFormModel formMod = oaFormModelService.selectByPrimaryKey(id);
        Map<String,Object> startNodeConfig = oaFormModelService.getStartElementConfigByFormModId(id);
        formMod.setStartNodeConfig(startNodeConfig);
        return ResponseUtil.success(formMod);

    }


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


    @GetMapping(value = "/formMods")
    @ApiOperation(value = "表单设计--获取表单列表结构数据", notes = "表单设计--获取表单列表结构数据")
    public ResponseEntity<Map> getAllFormModsList(){
        System.err.println("+++++++++++++++++++" + 1L);
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        HashMap dicParam = new HashMap(2);
        dicParam.put("tableName","tb_dic_form_type");
        dicParam.put("orderBy","sort");
        HashMap type = dicCommonService.selectAll(dicParam);
        List<DicCommon> typeList = (List<DicCommon>)type.get("content");

        System.err.println(typeList);
        // status如果等于0，则该条表单为假删除的表单，前端页面不该展示该表单;
        HashMap param = new HashMap(1);
        param.put("status","1");
        List<OaFormModel> modList = oaFormModelService.selectAll(param);
        List<HashMap<String, Object>> cats = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> mods = new ArrayList<HashMap<String, Object>>();
        for (OaFormModel oaFormMod : modList) {
            HashMap<String, Object> mod = new HashMap<String, Object>();
            mod.put("id", oaFormMod.getId());
            mod.put("name", oaFormMod.getName());
            mod.put("tableKey", oaFormMod.getTableKey());
            mods.add(mod);
        }
        HashMap<String, Object> catsMap = new HashMap<String, Object>();
        for (DicCommon oaFormModType : typeList) {
            HashMap<String, Object> cat = new HashMap<String, Object>();
            List<HashMap<String, Object>> catMap = new ArrayList<HashMap<String, Object>>();
            cat.put("id", oaFormModType.getId());
            cat.put("name", oaFormModType.getName());
            cats.add(cat);
            for (OaFormModel oaFormMod : modList) {
                List<String> typeIdsList = GuavaUtil.split2list(",", oaFormMod.getFormType());
                if (typeIdsList.contains(oaFormModType.getId().toString())) {
                    // 找到表单绑定的流程
                    OaFlow flow = oaFlowService.selectFlowByForm(oaFormMod.getId());
                    if (flow==null) {
                        // 如果表单已经绑定流程，则无法再绑定其他流程，普通表(type==1)
                        if (oaFormMod.getType() != null&&oaFormMod.getType() == 1) {
                            HashMap<String, Object> hm = new HashMap<String, Object>();
                            hm.put("id", oaFormMod.getId());
                            hm.put("catId", oaFormModType.getId());
                            hm.put("tableKey", oaFormMod.getTableKey());
                            if (oaFormMod.getDetailKeys() != null) {
                                hm.put("detailKeys", oaFormMod.getDetailKeys());
                            }
                            hm.put("name", oaFormMod.getName());
                            catMap.add(hm);
                        }
                    }
                }
            }
            catsMap.put(String.valueOf(oaFormModType.getId()), catMap);
        }

        hashMap.put("cats", cats);
        hashMap.put("mods", mods);
        hashMap.put("catsMap", catsMap);

        return ResponseUtil.success(hashMap);
    }

    @GetMapping(value = "/getFormListByAuth")
    @ApiOperation(value = "获取某用户可发起的表单列表", notes = "获取某用户可发起的表单列表")
    public ResponseEntity<Map> getFormListByAuth(){
        String userId = HttpServletUtils.getUserInfo().getUserId();
        try {
            Map result = oaFormModelService.getFormListByAuth(userId);
            return  ResponseUtil.success(result);
        }catch (Exception E){
            return ResponseUtil.error(E.getMessage());
        }

    }


}
