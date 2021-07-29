package com.cxtx.user_manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cxtx.common.domain.DicCommon;
import com.cxtx.common.domain.JwtModel;
import com.cxtx.common.service.DicCommonService;
import com.cxtx.common.unit.HttpServletUtils;
import com.cxtx.common.unit.ServiceUtil;
import com.cxtx.user_manage.domain.*;
import com.cxtx.user_manage.mapper.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.service.OaFormMainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Transactional(rollbackFor = Exception.class)
public class OaFormMainServiceImpl implements OaFormMainService {


    @Autowired
    private OaFormMainMapper oaFormMainMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private DicCommonService dicCommonService;

    @Autowired
    private OaFormModelMapper oaFormModelMapper;

    @Autowired
    private OaFormAddMapper oaFormAddMapper;


    @Override
    public int deleteByPrimaryKey(Long id) throws Exception{

        int num = 0;
        num = oaFormMainMapper.deleteByPrimaryKey(id);
        num = oaFormAddMapper.deleteByMainId(id);

        // 操作存在异常抛出，进行事务回滚
        if (num == 0) {
            throw new Exception("系统异常！");
        }
        return num;
    }


    @Override
    public int insertSelective(OaFormMain oaFormMain) throws Exception{
        JwtModel curUser = HttpServletUtils.getUserInfo();
        oaFormMain.setUpdateBy(Long.valueOf(curUser.getUserId()));
        oaFormMain.setCreateBy(Long.valueOf(curUser.getUserId()));
        int num = oaFormMainMapper.insertSelective(oaFormMain);
        int flag = 0;
        List<Map<String, Object>> tfList = JSONObject.parseObject(oaFormMain.getTypesForms().toString(), List.class);
        List<String> formIdList = new ArrayList<>();
        //配置可见类别
        for (Map<String, Object> tfMaps : tfList) {
            //type：0所选模块，1，全部模块,flag:0选择模块，1排除模块
            // 这个类型下排除所选表单，其他表单可见
            if ("0".equals(tfMaps.get("type")) && "1".equals(tfMaps.get("flag"))) {
                for (String typeId : (JSONObject.parseArray(tfMaps.get("typeIds").toString(), String.class))) {
                    HashMap param = new HashMap(2);
                    param.put("status","1");
                    param.put("formType",typeId);
                    List<OaFormModel> oaFormModelList = oaFormModelMapper.selectAll(param);
                    for (OaFormModel oaFormModel : oaFormModelList){
                        formIdList.add(oaFormModel.getId().toString());
                    }
                }

                //从符合类型的表单中去除又选择的表单
                //如果formIds有值，则是排除这些formIds,这个type其他表单都可见，如果formIds无值，这个type表单可见
                Iterator iterator = formIdList.iterator();
                while (iterator.hasNext()) {
                    String temp = (String) iterator.next();
                    List<String> listformids = JSONObject.parseArray(tfMaps.get("formIds").toString(), String.class);
                    for (String formId : listformids) {
                        if (formId.equals(temp)) {
                            iterator.remove();
                        }
                    }
                }
            }
            // 这个类型下选择全部模块
            if ("1".equals(tfMaps.get("type")) && "0".equals(tfMaps.get("flag"))) {
                for (String typeId : (JSONObject.parseArray(tfMaps.get("typeIds").toString(), String.class))) {
                    formIdList.add(typeId+"all");
                }
            }
            // 选择这个类型下某些表单可见
            if ("0".equals(tfMaps.get("type")) && "0".equals(tfMaps.get("flag"))) {
                for (String formId : (JSONObject.parseArray(tfMaps.get("formIds").toString(), String.class))) {
                    formIdList.add(formId);
                }
            }
        }
        //用户，部门，岗位，部门岗位
        Map<String, Object> authGroupMap = JSONObject.parseObject(oaFormMain.getAuthGroup().toString(), Map.class);
        if (formIdList.size() > 0) {
            for (String formId : formIdList) {
                // 选择全体人员，,用特殊标志标记 userId 设为ALL
                if ("1".equals(oaFormMain.getAuthAll())) {
                    OaFormAdd oaFormAdd = new OaFormAdd();
                    oaFormAdd.setFormId(formId);
                    oaFormAdd.setMainId(oaFormMain.getId());
                    oaFormAdd.setUserId("all");
                    oaFormAdd.setCreateBy(oaFormMain.getCreateBy());
                    oaFormAdd.setUpdateBy(oaFormMain.getUpdateBy());
                    flag = oaFormAddMapper.insertSelective(oaFormAdd);
                }

                // 未选择全体人员
                if ("0".equals(oaFormMain.getAuthAll())) {
                    if (authGroupMap.size() > 0) {
                        // 选择人员
                        if (!(null == authGroupMap.get("userIds") || "".equals(authGroupMap.get("userIds")))) {
                            List<String> userIds = (List<String>) authGroupMap.get("userIds");
                            for (String userId : userIds) {
                                OaFormAdd oaFormAdd = new OaFormAdd();
                                oaFormAdd.setFormId(formId);
                                oaFormAdd.setMainId(oaFormMain.getId());
                                oaFormAdd.setUserId(userId);
                                oaFormAdd.setCreateBy(oaFormMain.getCreateBy());
                                oaFormAdd.setUpdateBy(oaFormMain.getUpdateBy());
                                flag = oaFormAddMapper.insertSelective(oaFormAdd);
                            }
                        }
                        // 选择岗位
                        if (!(null == authGroupMap.get("roleIds") || "".equals(authGroupMap.get("roleIds")))) {
                            List<String> roleIds = (List<String>) authGroupMap.get("roleIds");
                            for (String roleId : roleIds) {
                                OaFormAdd oaFormAdd = new OaFormAdd();
                                oaFormAdd.setFormId(formId);
                                oaFormAdd.setMainId(oaFormMain.getId());
                                oaFormAdd.setRoleId(roleId);
                                oaFormAdd.setCreateBy(oaFormMain.getCreateBy());
                                oaFormAdd.setUpdateBy(oaFormMain.getUpdateBy());
                                flag = oaFormAddMapper.insertSelective(oaFormAdd);
                            }
                        }
                        // 选择部门
                        if (!(null == authGroupMap.get("groupIds") || "".equals(authGroupMap.get("groupIds")))) {
                            List<String> groupIds = (List<String>) authGroupMap.get("groupIds");
                            for (String groupId : groupIds) {
                                OaFormAdd oaFormAdd = new OaFormAdd();
                                oaFormAdd.setFormId(formId);
                                oaFormAdd.setMainId(oaFormMain.getId());
                                oaFormAdd.setDeptId(groupId);
                                oaFormAdd.setCreateBy(oaFormMain.getCreateBy());
                                oaFormAdd.setUpdateBy(oaFormMain.getUpdateBy());
                                flag = oaFormAddMapper.insertSelective(oaFormAdd);
                            }
                        }

                        if (!(null == authGroupMap.get("groupRoleIds")
                                || "".equals(authGroupMap.get("groupRoleIds")))) {
                            List<Map<String, Object>> groupRoleIds = (List<Map<String, Object>>) authGroupMap
                                    .get("groupRoleIds");
                            for (Map<String, Object> groupRoleMap : groupRoleIds) {
                                OaFormAdd oaFormAdd = new OaFormAdd();
                                oaFormAdd.setFormId(formId);
                                oaFormAdd.setDeptId(groupRoleMap.get("groupId").toString());
                                oaFormAdd.setRoleId(groupRoleMap.get("roleId").toString());
                                oaFormAdd.setMainId(oaFormMain.getId());
                                oaFormAdd.setCreateBy(oaFormMain.getCreateBy());
                                oaFormAdd.setUpdateBy(oaFormMain.getUpdateBy());
                                flag = oaFormAddMapper.insertSelective(oaFormAdd);
                            }
                        }

                    }
                }
            }
        } else {
            OaFormAdd oaFormAdd = new OaFormAdd();
            oaFormAdd.setMainId(oaFormMain.getId());
            oaFormAdd.setCreateBy(oaFormMain.getCreateBy());
            oaFormAdd.setUpdateBy(oaFormMain.getUpdateBy());
            flag = oaFormAddMapper.insertSelective(oaFormAdd);
        }

        //// 操作存在异常抛出，进行事务回滚
        if (num < 0) {
            throw new Exception("系统异常！");
        }
        if (flag == 0) {
            throw new Exception("系统异常！");
        }

        return flag;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public OaFormMain selectByPrimaryKey(Long id) {
        return oaFormMainMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(OaFormMain record) throws Exception{
        int num = 0;
        num = deleteByPrimaryKey(record.getId());
        if (num == 0) {
            throw new Exception("系统异常！");
        }
        record.setId(null);
        num = insertSelective(record);
        return num;
    }
    @Override
    public List<OaFormMain> selectAll() {
        return oaFormMainMapper.selectAll();
    }

    @Override
    public List<OaFormMain> list() {
        List<OaFormMain> list3 = new ArrayList<>();
        List<OaFormMain> oaFormMains = oaFormMainMapper.selectAll();
        for (OaFormMain oaFormMain1 : oaFormMains) {
            List<Map> list4 = new ArrayList<>();
            Map<String, Object> mainMap = JSONObject.parseObject(oaFormMain1.getAuthGroup().toString(), Map.class);
            // 转换权限组别中的信息
            if (mainMap != null) {

                //用户
                if (!(null == mainMap.get("userIds") || "".equals(mainMap.get("userIds")))) {

                    List<String> stringArr = (List<String>) mainMap.get("userIds");
                    List<String> finalUser = new ArrayList<String>();
                    for (String userId : stringArr) {
                        User sysUser= userMapper.selectUserById(userId);
                        if(sysUser!=null) {
                            finalUser.add(sysUser.getLoginId());
                        }
                    }
                    mainMap.put("userNames", finalUser);
                }
                //角色(岗位)
                if (!(null == mainMap.get("roleIds") || "".equals(mainMap.get("roleIds")))) {
                    List<String> stringArr = (List<String>) mainMap.get("roleIds");
                    List<String> finalRole = new ArrayList<String>();
                    for (String roleId : stringArr) {
                        Role role = roleMapper.selectRoleById(roleId);
                        if(role != null){
                            finalRole.add(role.getName());
                        }
                    }
                    mainMap.put("roleNames", finalRole);
                }
                //部门
                if (!(null == mainMap.get("groupIds") || "".equals(mainMap.get("groupIds")))) {
                    List<String> stringArr = (List<String>) mainMap.get("groupIds");
                    List<String> finalGroup = new ArrayList<String>();
                    for (String groupId : stringArr) {
                        Department department = departmentMapper.selectDepartmentById(groupId);
                        if(department != null){
                            finalGroup.add(department.getName());
                        }
                    }
                    mainMap.put("groupIdNames", finalGroup);
                }
                //部门，岗位（角色）
                if (!(null == mainMap.get("groupRoleIds") || "".equals(mainMap.get("groupRoleIds")))) {
                    List<Map<String, Object>> stringArr = (List<Map<String, Object>>) mainMap.get("groupRoleIds");
                    List<Map<String, Object>> finalGroupRole = new ArrayList<Map<String, Object>>();
                    for (Map<String, Object> groupRoleMap : stringArr) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("roleName", "");
                        map.put("groupName", "");
                        if (!(null == groupRoleMap.get("roleId") || "".equals(groupRoleMap.get("roleId")))) {
                            Role role = roleMapper.selectRoleById(groupRoleMap.get("roleId").toString());
                            if(role != null){
                                map.put("roleName", role.getName());
                            }
                        }
                        if (!(null == groupRoleMap.get("groupId") || "".equals(groupRoleMap.get("groupId")))) {
                            Department department = departmentMapper.selectDepartmentById(groupRoleMap.get("groupId").toString());
                            if(department != null){
                                map.put("groupName", department.getName());
                            }
                        }
                        finalGroupRole.add(map);
                    }

                    mainMap.put("groupRoleNames", finalGroupRole);
                }
                String mainMapString = JSON.toJSONString(mainMap);
                oaFormMain1.setAuthGroup(mainMapString);
            }
            // 转换可见类型/模块组别中的信息
            List<Map<String, Object>> list = JSONObject.parseObject(oaFormMain1.getTypesForms().toString(), List.class);
            if (list != null) {
                for (Map<String, Object> map : list) {
                    List<String> list2 = new ArrayList<>();
                    List<String> list6 = new ArrayList<>();
                    map.put("formName", "");
                    map.put("typeName", "");
                    //type：0所选模块，1，全部模块,flag:0选择模块，1排除模块
                    //排除全部模块
                    if("1".equals(map.get("type")) && "1".equals(map.get("flag"))) {
                        List<String> list5 = JSONObject.parseArray(map.get("typeIds").toString(), String.class);
                        for (String typeId : list5) {
                            HashMap param = new HashMap(2);
                            param.put("tableName","tb_dic_form_type");
                            param.put("id",typeId);
                            DicCommon dicCommon = dicCommonService.selectOneDic(param);
                            //排除某个表单类型或其类型下的某个表单被删除的情况
                            if(dicCommon!=null) {
                                list6.add(dicCommon.getName());

                            }
                        }
                        map.put("typeName", list6);
                    }
                    //排除所选模块
                    if (!("1".equals(map.get("type")) && "1".equals(map.get("flag")))) {
                        List<String> list5 = JSONObject.parseArray(map.get("typeIds").toString(), String.class);
                        for (String typeId : list5) {
                            HashMap param = new HashMap(2);
                            param.put("tableName","tb_dic_form_type");
                            param.put("id",typeId);
                            DicCommon dicCommon = dicCommonService.selectOneDic(param);
                            //排除某个表单类型或其类型下的某个表单被删除的情况
                            if(dicCommon!=null) {
                                list6.add(dicCommon.getName());

                            }
                        }
                        map.put("typeName", list6);

                        List<String> list1 = JSONObject.parseArray(map.get("formIds").toString(), String.class);
                        for (String formId : list1) {
                            OaFormModel oaFormModel = oaFormModelMapper.selectByPrimaryKey(Long.valueOf(formId));
                            //排除某个表单类型下的某个表单被删除的情况
                            if(oaFormModel!=null) {
                                list2.add(oaFormModel.getName());
                            }
                        }
                        map.put("formName", list2);
                    }
                    list4.add(map);
                }
                String String = JSON.toJSONString(list4);
                oaFormMain1.setTypesForms(String);

            }
            list3.add(oaFormMain1);
        }
        return list3;
    }

    @Override
    public PageInfo<OaFormMain> queryByPage(Map params) {
        ServiceUtil.checkPageParams(params);
        PageHelper.startPage(params);
        List<OaFormMain> labels = oaFormMainMapper.selectAll();
        return new PageInfo<OaFormMain>(labels);
    }
}