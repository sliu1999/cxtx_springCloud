package com.cxtx.user_manage.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cxtx.common.domain.DicCommon;
import com.cxtx.common.domain.JwtModel;
import com.cxtx.common.service.DicCommonService;
import com.cxtx.common.unit.ServiceUtil;
import com.cxtx.user_manage.domain.*;
import com.cxtx.user_manage.mapper.*;
import com.cxtx.user_manage.service.OaFormAddService;
import com.cxtx.user_manage.service.OaFormMainService;
import com.cxtx.user_manage.service.UserService;
import com.cxtx.user_manage.unit.GuavaUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.service.OaFormModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Transactional(rollbackFor = Exception.class)
public class OaFormModelServiceImpl implements OaFormModelService {


    @Autowired
    private OaFormModelMapper oaFormModelMapper;

    @Autowired
    private OaFlowMapper oaFlowMapper;

    @Autowired
    private OaFlowModelMapper oaFlowModelMapper;

    @Autowired
    private OaFlowModelElementMapper oaFlowModelElementMapper;

    @Autowired
    private OaFormMainMapper oaFormMainMapper;

    @Autowired
    private OaFormMainService oaFormMainService;

    @Autowired
    private UserService userService;

    @Autowired
    private OaFormAddService oaFormAddService;

    @Autowired
    private DicCommonService dicCommonService;


    @Override
    public int deleteByPrimaryKey(Long id) {
        return oaFormModelMapper.deleteByPrimaryKey(id);
    }


    @Override
    public int insertSelective(OaFormModel record) {
        return oaFormModelMapper.insertSelective(record);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public OaFormModel selectByPrimaryKey(Long id) {
        return oaFormModelMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(OaFormModel oaFormMod) throws Exception{
        OaFormModel oaFormMods = oaFormModelMapper.selectByPrimaryKey(oaFormMod.getId());
        // 没有修改表单类型的情况
        if (oaFormMods.getFormType().equals(oaFormMod.getFormType())) {
            return oaFormModelMapper.updateByPrimaryKeySelective(oaFormMod);
        }
        int num = oaFormModelMapper.updateByPrimaryKeySelective(oaFormMod);
        if (num > 0) {
            //如果此表单的类型改变了
            List<OaFormMain> oaFormMains = oaFormMainMapper.selectAll();
            //修改此表单的发起权限
            if (oaFormMains.size() > 0) {
                for (OaFormMain oaFormMain : oaFormMains) {
                    //循环操作所有的发起权限
                    List<Map<String, Object>> oaFormMainList = JSONObject
                            .parseObject(oaFormMain.getTypesForms().toString(), List.class);
                    if (oaFormMainList.size() > 0) {
                        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
                        for (Map<String, Object> map : oaFormMainList) {
                            //如果权限配置为选择这个类型下某个表单可见
                            if ("0".equals(map.get("type")) && "0".equals(map.get("flag"))) {
                                List<Long> formIdList = JSONObject.parseArray(map.get("formIds").toString(), Long.class);
                                if (formIdList.size() > 0) {
                                    //如果配置了可见表单为某些具体的表单
                                    Iterator<Long> iterator = formIdList.iterator();
                                    // 发起权限中删除改变了分类的表单的权限，用户手动按照新类型进行重新配置
                                    while (iterator.hasNext()) {
                                        Long temp = iterator.next();
                                        if (temp == oaFormMod.getId().longValue()) {
                                            //如果这次修改的表单这某个权限中为可见表单，则把它排出去
                                            iterator.remove();
                                        }
                                    }
                                    map.put("formIds", formIdList);
                                    result.add(map);
                                }
                                //如果权限配置为排除某个类型下所选表单，其他表单可见
                            } else if ("0".equals(map.get("type")) && "1".equals(map.get("flag"))) {    //排除所选模块
                                List<Long> formIdList = JSONObject.parseArray(map.get("formIds").toString(), Long.class);
                                if (formIdList.size() > 0) {
                                    //如果选择了排除哪些具体的表单
                                    Iterator<Long> iterator = formIdList.iterator();
                                    // 发起权限中删除改变了分类的表单的权限，用户手动按照新类型进行重新配置
                                    while (iterator.hasNext()) {
                                        Long temp = iterator.next();
                                        if (temp == oaFormMod.getId().longValue()) {
                                            iterator.remove();
                                        }
                                    }
                                    map.put("formIds", formIdList);
                                }

                                result.add(map);
                            } else {
                                //选择全部模块可见
                                result.add(map);
                            }
                        }
                        oaFormMain.setTypesForms(result.toString());
                        num = oaFormMainService.updateByPrimaryKeySelective(oaFormMain);
                    }
                }
            }
        }
        // 操作存在异常抛出，进行事务回滚
        if (num <= 0) {
            throw new Exception("系统异常！");
        }
        return num;
    }

    @Override
    public List<OaFormModel> selectAll(Map params) {
        return oaFormModelMapper.selectAll(params);
    }

    @Override
    public PageInfo<OaFormModel> queryByPage(Map params) {
        ServiceUtil.checkPageParams(params);
        PageHelper.startPage(params);
        List<OaFormModel> labels = oaFormModelMapper.selectAll(params);
        return new PageInfo<OaFormModel>(labels);
    }

    @Override
    public Map<String,Object> getStartElementConfigByFormModId(Long formModId){
        Map<String,Object> result = new HashMap<String,Object>();
        OaFlow flow = oaFlowMapper.selectFlowByForm(formModId);
        //判断表单是否绑定流程
        if(null == flow) {
            return new HashMap<String,Object>();
        }
        OaFlowModel flowMod = oaFlowModelMapper.selectByFlowId(flow.getId());
        Long flowModelId = flowMod.getId();
        // 获取开始节点的配置信息
        Map<String, Object> modConfigMap = oaFlowModelElementMapper.getNowNodeConfig(flowModelId, "startElement");
        Map<String,Object> modConfig = JSONObject.parseObject((String) modConfigMap.get("modConfig"), Map.class);
        String hideTable = (String) modConfig.get("hideTable");
        if (hideTable == null) {
            hideTable = "";
        }
        List<Map<String, Object>> hideTableDetail = (List<Map<String, Object>>) modConfig.get("hideTableDetail");
        result.put("hideTable", hideTable);
        result.put("hideTableDetail", hideTableDetail);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class )
    public Long insertFormData(Map<String, Object> dataMap, JwtModel curUser) throws Exception {
        //插入主表
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Map<String,Object> module = (Map<String, Object>) dataMap.get("module");
        //主表名
        String tableKey = (String) module.get("tableKey");
        //插入明细表
        String detailKey = (String) module.get("detailKeys");
        //明细表名集合
        List<String> detailKeys = GuavaUtil.split2list(",", detailKey);
        Map<String, Object> map = new HashMap<String,Object>();
        map.put("tableKey", tableKey);
        Map<String, Object> formData = (Map) dataMap.get("formData");
        ArrayList columns = new ArrayList();
        ArrayList values = new ArrayList();
        for (String key : formData.keySet()) {
            //筛出明细表数据
            if(detailKeys.contains(key)) {
                continue;
            }
            columns.add(key);
            values.add(formData.get(key));
        }
        //添加时间
        columns.add("create_date");
        values.add(sdf.format(date));
        //操作人ID
        columns.add("create_by");
        values.add(curUser.getUserId());

        //添加时间
        columns.add("update_date");
        values.add(sdf.format(date));
        //操作人ID
        columns.add("update_by");
        values.add(curUser.getUserId());

        columns.add("create_name");
        values.add(curUser.getUsername());

        map.put("columns", columns);
        map.put("values", values);
        int exeNum = oaFormModelMapper.insertForm(map);
        Object id = map.get("id");
        Long formId = Long.parseLong(id.toString());

        for (String key : detailKeys) {
            List<Map<String, Object>> detail = formData.get(key)==null?new ArrayList<>():(List) formData.get(key);
            for (int i = 0; i < detail.size(); i++) {
                Map<String, Object> dmap = new HashMap();
                dmap.put("tableKey", tableKey + "_" + key);
                ArrayList dcolumns = new ArrayList();
                ArrayList dvalues = new ArrayList();
                dcolumns.add(key + "_id");
                dvalues.add(id);
                for (String dkey : detail.get(i).keySet()) {
                    dcolumns.add(dkey);
                    dvalues.add(detail.get(i).get(dkey));
                }
                dcolumns.add("create_date");
                dvalues.add(sdf.format(date));

                dcolumns.add("create_by");
                dvalues.add(curUser.getUserId());

                dcolumns.add("create_name");
                dvalues.add(curUser.getUsername());
                dmap.put("columns", dcolumns);
                dmap.put("values", dvalues);
                if(exeNum>0){
                    exeNum = oaFormModelMapper.insertForm(dmap);
                }
            }

        }
        if(exeNum<=0) {
            throw new Exception();
        }
        return formId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateFormData(Map<String, Object> dataMap, JwtModel curUser) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Map<String, Object> map = new HashMap<String,Object>();
        //表单数据
        Map<String, Object> formData = (Map) dataMap.get("formData");
        //模型信息以及表关键字信息
        Map<String,Object> module = (Map<String, Object>) dataMap.get("module");
        //主表关键字
        String tableKey = (String)module.get("tableKey");
        //明细表关键字，多个用逗号隔开
        String detailKey = (String) module.get("detailKeys");
        //将明细表字符串信息转化成集合
        List<String> detailKeys = GuavaUtil.split2list(",", detailKey);
        map.put("tableKey", tableKey);
        map.put("id", formData.get("id"));
        String sql = "";
        for (String key : formData.keySet()) {
            //剔除明细表数据
            if(detailKeys.contains(key)) {
                continue;
            }
            if(null != formData.get(key)) {
                sql = sql + key + "='" + formData.get(key) + "',";
            }
        }
        sql = sql + "update_date"  + "='" + sdf.format(date) + "',";
        sql = sql + "update_by"  + "='" + curUser.getUserId() + "',";
        map.put("sql", sql.substring(0, sql.length() - 1));
        //更新主表信息
        oaFormModelMapper.updateForm(map);

        for (String key : detailKeys) {
            //根据明细表ID获取表单集合中对应的明细表数据
            List<Map<String, Object>> detail = (List) formData.get(key);
            for (int i = 0; i < detail.size(); i++) {
                Map<String, Object> dmap = new HashMap<String,Object>();
                dmap.put("tableKey", tableKey + "_" + key);
                String dsql = "";
                //明细表ID
                Long detailId = detail.get(i).get("id")==null?null:Long.parseLong(detail.get(i).get("id").toString());
                //不为空更新
                if(null != detailId) {
                    for (String dkey : detail.get(i).keySet()) {
                        if(null!=detail.get(i).get(dkey)) {
                            dsql = dsql + dkey + "='" + detail.get(i).get(dkey) + "',";
                        }
                    }
                    dmap.put("id", detailId);
                    dmap.put("sql", dsql.substring(0, dsql.length() - 1));
                    //更新明细表数据
                    oaFormModelMapper.updateDetailForm(dmap);
                }
                //为空则为新增
                if(null==detail.get(i).get("id")) {
                    ArrayList dcolumns = new ArrayList();
                    ArrayList dvalues = new ArrayList();
                    for (String dkey : detail.get(i).keySet()) {
                        dcolumns.add(dkey);
                        dvalues.add(detail.get(i).get(dkey));
                    }
                    //关联主表ID
                    dcolumns.add(key+"_id");
                    dvalues.add(formData.get("id"));

                    dcolumns.add("create_date");
                    dvalues.add(sdf.format(date));

                    dcolumns.add("create_by");
                    dvalues.add(curUser.getUserId());

                    dcolumns.add("create_name");
                    dvalues.add(curUser.getUsername());


                    dmap.put("columns", dcolumns);
                    dmap.put("values", dvalues);
                    oaFormModelMapper.insertForm(dmap);
                }
            }
        }
        return 1;
    }

    @Override
    public Map getFormListByAuth(String userId) {
        //获取用户角色，部门信息
        Map userMap = userService.queryUserDetailById(userId);
        //获取此用户可用的表单列表
        List<Map<String, Object>> formIdList = oaFormAddService.getFormIdByAuth(userMap);
        List<String> typeIdList = new ArrayList<>();
        List<Map<String, Object>> mods = new ArrayList<Map<String, Object>>();
        Set<String> typeset = new HashSet();
        // 查出可用表单的类型
        for (Map<String, Object> maps : formIdList) {
            if (maps.get("typeId") != null) {
                typeIdList.add(maps.get("typeId").toString());
            }
        }
        typeset.addAll(typeIdList);
        List<Map<String, Object>> finalList = new ArrayList<>();
        for (String tId : typeset) {
            HashMap param = new HashMap(2);
            param.put("tableName","tb_dic_form_type");
            param.put("id",tId);
            DicCommon oaFormModType = dicCommonService.selectOneDic(param);
            Map<String, Object> formTypeMap = new HashMap<String, Object>();
            formTypeMap.put("typeIds", tId);
            formTypeMap.put("typeName", oaFormModType.getName());
            List<Map<String, Object>> formIdNameList = new ArrayList<>();
            for (Map<String, Object> map : formIdList) {
                // 截取formId 后3位
                String typeId = "";
                if(map.get("id").toString().length()>3){
                    typeId = map.get("id").toString().substring(map.get("id").toString().length() - 3, map.get("id").toString().length());
                }
                if (tId.equals(map.get("typeId").toString()) && "all".equals(typeId.toString())) {
                    HashMap temp = new HashMap(2);
                    temp.put("formType",tId);
                    temp.put("type",1);
                    List<OaFormModel> oaFormModList = oaFormModelMapper.selectAll(temp);
                    if (oaFormModList.size() > 0) {
                        for (OaFormModel oaFormMod : oaFormModList) {
                            Map<String, Object> formIdNameMap = new HashMap<String, Object>();
                            formIdNameMap.put("formId", oaFormMod.getId());
                            formIdNameMap.put("id", oaFormMod.getId());
                            formIdNameMap.put("formName", oaFormMod.getName());
                            formIdNameMap.put("name", oaFormMod.getName());
                            formIdNameMap.put("tableKey",oaFormMod.getTableKey());
                            formIdNameList.add(formIdNameMap);
                        }
                    }
                } else {
                    if (tId.equals(map.get("typeId").toString())) {
                        if(Integer.parseInt(map.get("status").toString())==1) {
                            Map<String, Object> formIdNameMap = new HashMap<String, Object>();
                            formIdNameMap.put("formId", map.get("id").toString());
                            formIdNameMap.put("id", map.get("id").toString());
                            formIdNameMap.put("formName", map.get("name"));
                            formIdNameMap.put("name", map.get("name"));
                            formIdNameMap.put("tableKey", map.get("tableKey"));
                            formIdNameList.add(formIdNameMap);
                        }
                    }
                }
            }
            // List<map>根据map的key值去重
            List<Map<String, Object>> list = removeRepeatMapByKey(formIdNameList, "formName");
            mods.addAll(list);
            formTypeMap.put("formIdName", list);
            finalList.add(formTypeMap);
        }

        HashMap<String, Object> finalMap = new HashMap<String, Object>();

        finalMap.put("mods", mods);
        finalMap.put("catsList", finalList);
        return finalMap;
    }

    /**
     * 根据map中的某个key 去除List中重复的map
     */
    public static List<Map<String, Object>> removeRepeatMapByKey(List<Map<String, Object>> list, String mapKey) {
        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, Map> msp = new HashMap<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            Map map = list.get(i);
            String id = (String) map.get(mapKey);
            map.remove(mapKey);
            msp.put(id, map);
        }
        Set<String> mspKey = msp.keySet();
        for (String key : mspKey) {
            Map newMap = msp.get(key);
            newMap.put(mapKey, key);
            listMap.add(newMap);
        }
        return listMap;
    }

}