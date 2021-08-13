package com.cxtx.user_manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cxtx.common.domain.JwtModel;
import com.cxtx.common.unit.HttpServletUtils;
import com.cxtx.common.unit.ServiceUtil;
import com.cxtx.user_manage.domain.*;
import com.cxtx.user_manage.mapper.*;
import com.cxtx.user_manage.service.*;
import com.cxtx.user_manage.unit.GuavaUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Transactional(rollbackFor = Exception.class)
public class OaServiceImpl implements OaService {

    @Autowired
    private OaFlowService oaFlowService;

    @Autowired
    private OaFlowModelService oaFlowModelService;

    @Autowired
    private OaFlowModelElementService oaFlowModelElementService;

    @Autowired
    private OaFlowModelElementMapper oaFlowModelElementMapper;

    @Autowired
    private OaFlowModelElementConfigMapper oaFlowModelElementConfigMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OaFormModelService oaFormModelService;

    @Autowired
    private OaProcessService oaProcessService;

    @Autowired
    private OaProcessRunService oaProcessRunService;

    @Autowired
    private OaProcessHisService oaProcessHisService;

    @Autowired
    private OaProcessLogService oaProcessLogService;

    @Autowired
    private OaFormProcessInstanceService oaFormProcessInstanceService;

    @Autowired
    private OaFlowFormService oaFlowFormService;

    @Autowired
    private AppService appService;

    /**
     * 获取审批人
     * @param elementConfig
     * @param curUserId
     * @return
     */
    @Override
    public List<User> getHandler(Map<String, Object> elementConfig, Long curUserId) {
        // 判断elementConfig中的roleid和deptid是否为byCreator
        //部门岗位
        List<Map<String, Object>> groupRoleIds = (List<Map<String, Object>>) elementConfig.get("groupRoleIds");
        for (int i = 0; i < groupRoleIds.size(); i++) {
            Map<String, Object> groupRole = groupRoleIds.get(i);
            String group_id = groupRole.get("groupId").equals("") ? null : groupRole.get("groupId").toString();
            String role_id = groupRole.get("roleId").equals("") ? null : groupRole.get("roleId").toString();
            // 根据创建人ID获取部门岗位信息
            Map<String, Object> userMap = userService.queryUserDetailById(curUserId.toString());
            // 根据发起人部门
            if ("byCreator".equals(group_id)) {
                groupRole.put("groupId", userMap.get("deptId"));
            }
            // 根据发起人
            if ("byCreator".equals(role_id)) {
                groupRole.put("roleId", userMap.get("roleId"));
            }
            // 根据发起人及其上级部门
            if ("groupIdAndParent".equals(group_id)) {
                groupRole.put("groupId", userMap.get("deptId") + "," + userMap.get("parentDeptId"));
            }
            groupRoleIds.set(i, groupRole);
        }
        // 审批人配置
        try {
            Set<User> users = new HashSet<User>();
            // 提交给发起人
            //获取是否配置了提交给发起人
            Boolean submitInitiator = elementConfig.get("submitInitiator") == null ? false
                    : (Boolean) elementConfig.get("submitInitiator");
            if (submitInitiator) {
                //如果勾选了
                User creator = userMapper.selectUserById(curUserId.toString());
                users.add(creator);
            }
            // 审批人配置部门岗位
            for (Map<String, Object> groupRole : groupRoleIds) {

                String group_id = groupRole.get("groupId").equals("") ? null : groupRole.get("groupId").toString();
                String role_id = groupRole.get("roleId").equals("") ? null : groupRole.get("roleId").toString();
                if (group_id != null && role_id != null) {
                    if(group_id.startsWith("[")&&group_id.endsWith("]")) {
                        group_id = group_id.replaceAll("\\[", "").replaceAll("\\]", "");
                    }
                    if(role_id.startsWith("[")&&role_id.endsWith("]")) {
                        role_id = role_id.replaceAll("\\[", "").replaceAll("\\]", "");
                    }
                    if ((group_id != null&&!"".equals(group_id)) && (role_id != null&&!"".equals(role_id))) {
                        List<User> user = userService.getUserByGroupRole(group_id, role_id);
                        users.addAll(user);
                    }
                }

            }
            // 审批人仅根据指定部门
            List<String> groupIds = elementConfig.get("groupIds") == null ? new ArrayList<String>()
                    : (List<String>) elementConfig.get("groupIds");
            for (String groupId : groupIds) {
                List<User> user = userService.getUserByGroup(Long.parseLong(groupId));
                users.addAll(user);
            }
            // 审批人仅根据指定岗位
            List<String> roleIds = elementConfig.get("roleIds") == null ? new ArrayList<String>()
                    : (List<String>) elementConfig.get("roleIds");
            for (String roleId : roleIds) {
                List<User> user = userService.getUserByRole(Long.parseLong(roleId));
                users.addAll(user);
            }
            // 审批人根据指定人员
            List<String> userIds = elementConfig.get("userIds") == null ? new ArrayList<String>()
                    : (List<String>) elementConfig.get("userIds");
            for (String userId : userIds) {
                User user = userService.selectUserById(userId);
                users.add(user);
            }
            //总体根据userId去重
            Set<User> userSet = new TreeSet<User>(new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    return o1.getId().compareTo(o2.getId());
                }
            });
            userSet.addAll(users);
            return new ArrayList<>(userSet);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map submit(Map<String, Object> payload, JwtModel curUser) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        // 流程审批人,先看参数中是否规定了流程审批人
        List<Map<String, Object>> assigneeList = payload.get("assignee") == null ? null
                : (List<Map<String, Object>>) payload.get("assignee");
        String json = JSON.toJSONString(assigneeList);
        //系统通知配置
        Map<String,Object> notifyConfig = payload.get("notifyConfig")==null?null:(Map<String,Object>)payload.get("notifyConfig");
        List<User> assignee = payload.get("assignee") == null ? new ArrayList<User>()
                : JSON.parseArray(json, User.class);
        int exeNum = 0;
        // 流程实例ID
        Long processId = null;
        // 当前登录人ID
        Long curUserId = Long.valueOf(curUser.getUserId());
        // 获取当前表单模块
        Map<String, Object> module = (Map<String, Object>) payload.get("module");
        // 表关键字
        String tableKey = (String) module.get("tableKey");
        // 模型的ID
        Long formModId = Long.parseLong(module.get("id").toString());
        //查询表单模型绑定的流程
        OaFlow flow = oaFlowService.selectFlowByForm(formModId);
        String flowName = flow.getFlowName();
        OaFlowModel flowMod = oaFlowModelService.selectByFlowId(flow.getId());
        // 流程模型的ID
        Long modId = flowMod.getId();

        Long flowId = flowMod.getFlowId();
        // 表单数据
        Map<String, Object> formData = (Map<String, Object>) payload.get("formData");
        formData.put("create_by", curUserId);
        // 流程实例
        OaProcess process = new OaProcess();
        // 流程ID
        process.setFlowId(flowId);
        // 设置节点为开始节点
        process.setCode("startElement");

        // 表单模型ID
        process.setFlowModelId(modId);
        process.setCreateBy(curUserId);
        // 设置流程实例的初始状态
        process.setState(0);
        // 设置流程名称
        process.setProcessName(flowMod.getFlowName());
        // 获取开始节点的配置信息
        Map<String, Object> modConfigMap = oaFlowModelElementService.getNowNodeConfig("startElement",modId);
        //获取元素配置信息
        Map<String, Object> modConfig = JSONObject.parseObject((String) modConfigMap.get("modConfig"), Map.class);
        //获取节点配置-审批配置-通知指定人
        String designee = modConfig.get("designee") ==null?null:modConfig.get("designee").toString();
        //获取是否通知发起人配置
        Boolean initiator = modConfig.get("initiator") ==null?false:(Boolean)modConfig.get("initiator");

        // 获取下一节点
        OaFlowModelElement nextElement = new OaFlowModelElement();
        // 通过当前节点获取下一节点
        OaFlowModelElement modElement = getNextNodes("startElement", modId, formData, curUserId);
        if (null == modElement) {
            throw new Exception("下一节点为空！");
        }


        nextElement = modElement;

        // 获取下一节点审批人信息
        String nextCode = nextElement.getCode();
        String nextCodeName = nextElement.getName();
        process.setCode(nextCode);
        process.setCodeName(nextCodeName);
        // 获取下一节点的配置
        Map<String, Object> configMap = JSONObject.parseObject(nextElement.getParam(), Map.class);
        List<Object> handlerList = new ArrayList<Object>();
        //下一节点审批人
        List<User> nextNodeAssignee = new ArrayList<User>();
        // 审批人是否多选
        Boolean handlerSelectable = configMap.get("handlerSelectable") == null ? false
                : (Boolean) configMap.get("handlerSelectable");
        // 判断前端是否选择下一节点审批人
        if ((null == assignee || assignee.size() == 0) && !"endElement".equals(nextCode)) {
            //如果前端没指定下一审批人，并且下一节点不是结束节点
            // 解析审批人配置获取审批人
            List<User> handlers = getHandler(configMap, curUserId);
            if (null == handlers || handlers.isEmpty()) {
                throw new Exception("未找到审批人！");
            }
            for (User user : handlers) {
                handlerList.add(user.getId());
            }
            // 如果有多个审批人并且流程设置审批人可选
            if (handlers.size() > 1 && handlerSelectable) {
                Map<String, Object> assigneeMap = new HashMap<String, Object>();
                assigneeMap.put("assignee", handlers);
                assigneeMap.put("handlerSelectConfig", configMap.get("handlerSelectConfig"));
                HashMap TEMP = new HashMap();
                TEMP.put("successStatus","2");
                TEMP.put("message","请选择审批人");
                TEMP.put("data",assigneeMap);
                return TEMP;
            } else {
                nextNodeAssignee = handlers;
                process.setAssignee(GuavaUtil.list3string(handlerList, ","));
            }
            result.put("handlers", handlers);

        } else {
            // 如果发起节点的下一节点是结束节点，则直接将流程状态设置成以完成状态
            if ("endElement".equals(nextCode)) {
                process.setState(4);
            } else {
                for (User user : assignee) {
                    handlerList.add(user.getId());
                }
                nextNodeAssignee = assignee;
                process.setAssignee(GuavaUtil.list3string(handlerList, ","));
                result.put("handlers", assignee);
            }
        }
        formData.remove("create_by");
        // 向表单中填充数据
        Long formId = oaFormModelService.insertFormData(payload, curUser);
        // APP表单ID
        process.setAppFormId(formId);
        process.setCreateBy(Long.valueOf(curUser.getUserId()));
        process.setUpdateBy(Long.valueOf(curUser.getUserId()));
        // 创建一个流程实例
        exeNum = oaProcessService.insertSelective(process);
        processId = process.getId();
        result.put("nodeId", nextCode);
        result.put("processId", processId);
        // 待审批人ID集合
        for (User user : nextNodeAssignee) {
            OaProcessRun runProcess = new OaProcessRun();
            runProcess.setProcessId(processId);
            runProcess.setUserId(Long.valueOf(user.getId()));
            // 添加数据到待处理流程表中
            if (exeNum > 0){
                runProcess.setCreateBy(Long.valueOf(curUser.getUserId()));
                runProcess.setUpdateBy(Long.valueOf(curUser.getUserId()));
                exeNum = oaProcessRunService.insertSelective(runProcess);
            }
        }

        // 向流程实例表中添加一个流程实例
        OaProcessHis hisProcess = new OaProcessHis();
        hisProcess.setProcessId(processId);
        hisProcess.setUserId(curUserId);
        // 添加数据到历史流程处理表中
        if (exeNum > 0){
            hisProcess.setCreateBy(Long.valueOf(curUser.getUserId()));
            hisProcess.setUpdateBy(Long.valueOf(curUser.getUserId()));
            exeNum = oaProcessHisService.insertSelective(hisProcess);
        }

        OaProcessLog processLog = new OaProcessLog();
        processLog.setProcessId(processId);
        processLog.setCurAssignee(curUserId.toString());
        processLog.setCurNode("startElement");
        processLog.setFlowId(flowId);
        processLog.setNextNode(nextCode);
        processLog.setNextAssignees(GuavaUtil.list3string(handlerList, ","));
        processLog.setActionId(1);
        // 插入流程日志
        processLog.setCreateBy(Long.valueOf(curUser.getUserId()));
        processLog.setUpdateBy(Long.valueOf(curUser.getUserId()));
        exeNum = oaProcessLogService.insertSelective(processLog);
        OaFormProcessInstance oaFormProcessInstance = new OaFormProcessInstance();
        oaFormProcessInstance.setAppFormId(formId);
        oaFormProcessInstance.setFormModelId(formModId);
        oaFormProcessInstance.setUserId(curUserId);
        oaFormProcessInstance.setProcessId(processId);
        oaFormProcessInstance.setProcessName(flowName);
        oaFormProcessInstance.setCreateBy(Long.valueOf(curUser.getUserId()));
        oaFormProcessInstance.setUpdateBy(Long.valueOf(curUser.getUserId()));
        oaFormProcessInstanceService.insertSelective(oaFormProcessInstance);
        // 数据保存异常进行事务回滚
        if (exeNum <= 0){
            throw new Exception();
        }
        //针对发起节点的下一个节点就是结束节点的流程提醒
        if ("endElement".equals(nextCode)) {
            HashMap TEMP = new HashMap();
            TEMP.put("message","提交成功");
            TEMP.put("successStatus","1");
            return TEMP;
        }
        //流程发起过程中配置的系统通知
        Boolean sendNotify = true;
        if(null!=notifyConfig) {
            String notifyTitle = notifyConfig.get("notifyTitle")==null?flowMod.getFlowName():notifyConfig.get("notifyTitle").toString();
            String notifiedId = notifyConfig.get("notifiedId")==null?null:notifyConfig.get("notifiedId").toString();
            if(null!=notifiedId&&!notifiedId.isEmpty()) {
                List<User> notifiedUser = userMapper.getUserInfoByIdStrings(notifiedId);
                //通知的逻辑
            }

        }
        //通知发起人或指定人
        if(initiator||(null!=designee&&!designee.isEmpty())) {
            Set<User> userSet = new HashSet<User>();
            //流程发起人
            if(initiator) {
                userSet.add(userService.selectUserById(curUserId.toString()));
            }
            if(null!=designee&&!designee.isEmpty()) {
                List<User> notifiedUser = userMapper.getUserInfoByIdStrings(designee);
                userSet.addAll(notifiedUser);
            }
            for(User user:userSet) {
               //通知的逻辑
            }
        }
        result.put("message","发起系统通知失败！流程提交成功并转移至下一节点:"+nextCodeName);
        result.put("successStatus","1");
        return result;
    }

    /**
     * 根据当前节点获取下一节点，最重要的内容
     * @param curCode
     * @param modId
     * @param formData
     * @param userId
     * @return
     * @throws ScriptException
     */
    @Override
    public OaFlowModelElement getNextNodes(String curCode, Long modId, Map<String, Object> formData, Long userId) throws ScriptException {
        //获取当前节点涉及到的节点元素列表
        List<OaFlowModelDetail> elements = oaFlowModelElementMapper.getLastNextNodes(modId, curCode);
        //获取当前节点配置
        Map<String, Object> map = oaFlowModelElementMapper.getNowNodeConfig(modId, curCode);
        Map<String, Object> configMap = new HashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        //如果当前节点有配置信息
        if (null != map && elements.size() != 0) {
            //如果当前节点的下一节点是多个
            if (null != map.get("modConfig") && !("".equals(map.get("modConfig"))) && elements.size() > 1) {
                //获取节点配置信息
                configMap = JSON.parseObject(map.get("modConfig").toString(), HashMap.class);
                if (null != configMap.get("toNode") && !("".equals(configMap.get("toNode")))) {
                    //获取当前节点的下一节点指向
                    list = JSON.parseObject(configMap.get("toNode").toString(), ArrayList.class);

                    //java 与 js 的互通类
                    ScriptEngineManager manager = new ScriptEngineManager();
                    ScriptEngine engine = manager.getEngineByName("javascript");
                    StringBuilder javascript = new StringBuilder("function time(date){ return +(new Date(date)); }");
                    javascript.append("var userInfo = {id:").append(userId).append(",").append("}");
                    //eval()用于执行js脚本
                    engine.eval(javascript.toString());

                    for (String key : formData.keySet()) {
                        Object value = formData.get(key);
                        if (!(value instanceof List)) {
                            engine.put(key, value);
                        }
                    }
                    // 默认节点
                    String defaultNodeId = null;
                    String nextNodeId = null;
                    boolean ifDefault = false;
                    boolean flag = false;
                    //循环下一节点指向
                    for (Map<String, Object> nextNode : list) {
                        nextNode.get("condition");
                        engine.eval("flag=("
                                + (nextNode.get("condition").equals("default") ? false : nextNode.get("condition"))
                                + ")");
                        engine.eval(
                                "ifDefault = (" + (nextNode.get("condition").equals("default") ? true : false) + ")");
                        flag = (boolean) engine.get("flag");
                        if (!ifDefault) {
                            ifDefault = (boolean) engine.get("ifDefault");
                            if (ifDefault) {
                                defaultNodeId = (String) nextNode.get("code");
                            }
                        }
                        // 优先选择满足条件的节点
                        if (flag) {
                            nextNodeId = (String) nextNode.get("code");
                            for (OaFlowModelDetail oaFlowModelDetail : elements) {
                                if (oaFlowModelDetail.getNextNodeCode().equals(nextNodeId)) {
                                    return getNode(curCode, modId, oaFlowModelDetail);
                                }
                            }
                        }
                    }
                    // 没有满足条件的节点则选择默认节点
                    if (ifDefault) {
                        for (OaFlowModelDetail oaFlowModelDetail : elements) {
                            if (oaFlowModelDetail.getNextNodeCode().equals(defaultNodeId)) {
                                return getNode(curCode, modId, oaFlowModelDetail);
                            }
                        }
                    }
                } else {
                    return getNode(curCode, modId, elements.get(0));
                }
            } else {
                //如果当前节点没有配置信息，则下一节点去默认第一个
                return getNode(curCode, modId, elements.get(0));
            }
        } else {
            return null;
        }
        return null;
    }

    public OaFlowModelElement getNode(String curNode, Long modId, OaFlowModelDetail oaFlowModelDetail) {
        OaFlowModelElement element = oaFlowModelElementMapper.selectOneByCodeAndModelId(oaFlowModelDetail.getNextNodeCode(),modId);
        OaFlowModelElementConfig config = oaFlowModelElementConfigMapper.selectByElementId(element.getId());
        // 节点的配置信息
        element.setParam(config.getElementConfig());
        return element;
    }

    @Override
    public Map getProcessDetail(Long processId) {
        Map<String, Object> result = new HashMap<String, Object>();
        // 查询流程实例
        OaProcess process = oaProcessService.selectByPrimaryKey(processId);
        List<OaProcessLog> logs = oaProcessLogService.getLogByProcessId(processId);
        Map<String, Object> module = new HashMap<String, Object>();
        Date processDate = process.getCreateDate();

        // 流程模型ID
        Long flowId = process.getFlowId();
        System.err.println(flowId);
        // app表单实例ID
        Long formId = process.getAppFormId();
        Long flowModId = process.getFlowModelId();

        OaFlowForm flowForm = oaFlowFormService.selectByFlowModelId(flowModId);
        String code = process.getCode();
        OaFormModel formMod = oaFormModelService.selectByPrimaryKey(flowForm.getFormModelId());
        Map<String, Object> config = oaFlowModelElementService.getNowNodeConfig(code,flowModId);
        // 解析节点配置json字符串
        Map<String, Object> modElementConfig = JSONObject.parseObject(config.get("modConfig").toString(), Map.class);
        // 审批意见必填
        Boolean isRequired = modElementConfig.get("isRequired") == null ? false
                : (Boolean) modElementConfig.get("isRequired");
        String tableKey = formMod.getTableKey();
        module.put("tableKey", tableKey);
        module.put("detailKeys", formMod.getDetailKeys());
        module.put("id", formMod.getId());
        List<Map<String,String>> columnList = appService.getTableColumnList("_app_"+tableKey);
        List<String> columns = new ArrayList<>();
        for (Map<String,String> column : columnList){
            if("datetime".equals(column.get("dataType")) || "timestamp".equals(column.get("dataType"))){
                String columnFormat = "DATE_FORMAT("+column.get("columnName")+",'%Y-%m-%d %H:%i:%s') as "+column.get("columnName");
                columns.add(columnFormat);
            }else {
                columns.add(column.get("columnName"));
            }
        }
        String columnStr = StringUtils.join(columns,",");
        String sql = "select "+columnStr+" from _app_" + tableKey + " where id = " + formId;
        Map<String, Object> formData = appService.getMap(sql);
        String detailKeys = formMod.getDetailKeys();
        List<String> detailKeyList = GuavaUtil.split2list(",", detailKeys);
        // 获取主表相关的明细表数据
        for (String dKey : detailKeyList) {
            List<Map<String,String>> columnDetailList = appService.getTableColumnList("_app_"+tableKey+"_" + dKey);
            List<String> columnDetails = new ArrayList<>();
            for (Map<String,String> column : columnDetailList){
                if("datetime".equals(column.get("dataType")) || "timestamp".equals(column.get("dataType"))){
                    String columnFormat = "DATE_FORMAT("+column.get("columnName")+",'%Y-%m-%d %H:%i:%s') as "+column.get("columnName");
                    columnDetails.add(columnFormat);
                }else {
                    columnDetails.add(column.get("columnName"));
                }
            }
            String columnDetailStr = StringUtils.join(columnDetails,",");

            String dSql = "select "+columnDetailStr+" from _app_" + tableKey + "_" + dKey + " where " + dKey + "_id = "
                    + formData.get("id");
            List<Map<String, Object>> dValue = appService.getInfoBySql(dSql);
            formData.put(dKey, dValue);
        }

        // 获取每个流程各个节点的表单配置信息
        OaFlow flowDto = oaFlowService.newFindFlow(flowId, processDate);
        GuavaUtil.entity2map(flowDto);
        List<Map<String, Object>> modElements = JSONObject
                .parseObject(GuavaUtil.entity2map(flowDto).get("modElements").toString(), List.class);
        for (Map<String, Object> modElement : modElements) {
            // 获取待办事项当前任务节点下的表单配置信息
            if (modElement.get("code").equals(process.getCode())) {
                HashMap<String, Object> modElementInfo = new HashMap<String, Object>();
                Map<String, Object> Config = (Map<String, Object>) modElement.get("modElementConfig");
                String editTable = (String) Config.get("editTable");
                if (editTable == null) {
                    editTable = "";
                }
                List<Map<String, Object>> editTableDetail = (List<Map<String, Object>>) Config.get("editTableDetail");
                String hideTable = (String) Config.get("hideTable");
                if (hideTable == null) {
                    hideTable = "";
                }
                List<Map<String, Object>> hideTableDetail = (List<Map<String, Object>>) Config.get("hideTableDetail");
                modElementInfo.put("editTable", editTable);
                modElementInfo.put("editTableDetail", editTableDetail);
                modElementInfo.put("hideTable", hideTable);
                modElementInfo.put("hideTableDetail", hideTableDetail);
                result.put("flowFormConfig", modElementInfo);
            }
        }
        result.put("process", process);
        result.put("formData", formData);
        result.put("tableSchema", formMod.getTableSchema());
        result.put("module", module);
        result.put("tableKey", tableKey);
        result.put("formView", formMod.getFormView());
        result.put("name", formMod.getName());
        result.put("logs", logs);
        result.put("isRequired", isRequired);
        return result;
    }

    @Override
    public Map getProcessDetailTwo(Long processId) {
        Map<String, Object> result = new HashMap<String, Object>();
        // 查询流程实例
        OaProcess process = oaProcessService.selectByPrimaryKey(processId);
        List<OaProcessLog> logs = oaProcessLogService.getLogByProcessId(processId);
        Map<String, Object> module = new HashMap<String, Object>();
        Date processDate = process.getCreateDate();

        // 流程模型ID
        Long flowId = process.getFlowId();
        System.err.println(flowId);
        // app表单实例ID
        Long formId = process.getAppFormId();
        Long flowModId = process.getFlowModelId();

        OaFlowForm flowForm = oaFlowFormService.selectByFlowModelId(flowModId);
        String code = process.getCode();
        OaFormModel formMod = oaFormModelService.selectByPrimaryKey(flowForm.getFormModelId());
        Map<String, Object> config = oaFlowModelElementService.getNowNodeConfig(code,flowModId);
        // 解析节点配置json字符串
        Map<String, Object> modElementConfig = JSONObject.parseObject(config.get("modConfig").toString(), Map.class);
        // 审批意见必填
        Boolean isRequired = modElementConfig.get("isRequired") == null ? false
                : (Boolean) modElementConfig.get("isRequired");
        String tableKey = formMod.getTableKey();
        module.put("tableKey", tableKey);
        module.put("detailKeys", formMod.getDetailKeys());
        module.put("id", formMod.getId());
        String sql = "select * from _app_" + tableKey + " where id = " + formId;
        Map<String, Object> formData = appService.getMap(sql);
        String detailKeys = formMod.getDetailKeys();
        List<String> detailKeyList = GuavaUtil.split2list(",", detailKeys);
        // 获取主表相关的明细表数据
        for (String dKey : detailKeyList) {
            String dSql = "select * from _app_" + tableKey + "_" + dKey + " where " + dKey + "_id = "
                    + formData.get("id");
            List<Map<String, Object>> dValue = appService.getInfoBySql(dSql);
            formData.put(dKey, dValue);
        }

        // 获取每个流程各个节点的表单配置信息
        OaFlow flowDto = oaFlowService.newFindFlow(flowId, processDate);
        GuavaUtil.entity2map(flowDto);
        List<Map<String, Object>> modElements = JSONObject
                .parseObject(GuavaUtil.entity2map(flowDto).get("modElements").toString(), List.class);
        for (Map<String, Object> modElement : modElements) {
            // 获取待办事项当前任务节点下的表单配置信息
            if (modElement.get("code").equals(process.getCode())) {
                HashMap<String, Object> modElementInfo = new HashMap<String, Object>();
                Map<String, Object> Config = (Map<String, Object>) modElement.get("modElementConfig");
                String editTable = (String) Config.get("editTable");
                if (editTable == null) {
                    editTable = "";
                }
                List<Map<String, Object>> editTableDetail = (List<Map<String, Object>>) Config.get("editTableDetail");
                String hideTable = (String) Config.get("hideTable");
                if (hideTable == null) {
                    hideTable = "";
                }
                List<Map<String, Object>> hideTableDetail = (List<Map<String, Object>>) Config.get("hideTableDetail");
                modElementInfo.put("editTable", editTable);
                modElementInfo.put("editTableDetail", editTableDetail);
                modElementInfo.put("hideTable", hideTable);
                modElementInfo.put("hideTableDetail", hideTableDetail);
                result.put("flowFormConfig", modElementInfo);
            }
        }
        result.put("process", process);
        result.put("formData", formData);
        result.put("tableSchema", formMod.getTableSchema());
        result.put("module", module);
        result.put("tableKey", tableKey);
        result.put("formView", formMod.getFormView());
        result.put("name", formMod.getName());
        result.put("logs", logs);
        result.put("isRequired", isRequired);
        return result;
    }

    @Override
    public Map getHisProcessDetail(Long processId) {
        JwtModel curUser = HttpServletUtils.getUserInfo();
        Map<String, Object> result = new HashMap<String, Object>();
        // 查询流程实例
        OaProcess process = oaProcessService.selectByPrimaryKey(processId);
        List<OaProcessLog> logs = oaProcessLogService.getLogByProcessId(processId);
        Map<String, Object> module = new HashMap<String, Object>();

        // 流程模型ID
        Long flowId = process.getFlowId();
        System.err.println(flowId);
        // 表单实例ID
        Long formId = process.getAppFormId();
        Long modId = process.getFlowModelId();
        String code = process.getCode();
        OaFormModel formMod = oaFormModelService.getFormModByFlowModId(modId);
        Map<String, Object> config = oaFlowModelElementService.getNowNodeConfig(code,modId);
        // 解析节点配置json字符串
        Map<String, Object> modElementConfig = JSONObject.parseObject(config.get("modConfig").toString(), Map.class);
        // 审批意见必填
        Boolean isRequired = modElementConfig.get("isRequired") == null ? false
                : (Boolean) modElementConfig.get("isRequired");
        String tableKey = formMod.getTableKey();
        module.put("tableKey", tableKey);
        module.put("detailKeys", formMod.getDetailKeys());
        module.put("id", formMod.getId());
        List<Map<String,String>> columnList = appService.getTableColumnList("_app_"+tableKey);
        List<String> columns = new ArrayList<>();
        for (Map<String,String> column : columnList){
            if("datetime".equals(column.get("dataType")) || "timestamp".equals(column.get("dataType"))){
                String columnFormat = "DATE_FORMAT("+column.get("columnName")+",'%y-%m-%d %H:%i:%s') as "+column.get("columnName");
                columns.add(columnFormat);
            }else {
                columns.add(column.get("columnName"));
            }
        }
        String columnStr = StringUtils.join(columns,",");
        String sql = "select "+columnStr+" from _app_" + tableKey + " where id = " + formId;
        Map<String, Object> formData = appService.getMap(sql);
        String detailKeys = formMod.getDetailKeys();
        List<String> detailKeyList = GuavaUtil.split2list(",", detailKeys);
        // 获取主表相关的明细表数据
        for (String dKey : detailKeyList) {
            List<Map<String,String>> columnDetailList = appService.getTableColumnList("_app_"+tableKey+"_" + dKey);
            List<String> columnDetails = new ArrayList<>();
            for (Map<String,String> column : columnDetailList){
                if("datetime".equals(column.get("dataType")) || "timestamp".equals(column.get("dataType"))){
                    String columnFormat = "DATE_FORMAT("+column.get("columnName")+",'%y-%m-%d %H:%i:%s') as "+column.get("columnName");
                    columnDetails.add(columnFormat);
                }else {
                    columnDetails.add(column.get("columnName"));
                }
            }
            String columnDetailStr = StringUtils.join(columnDetails,",");

            String dSql = "select "+columnDetailStr+" from _app_" + tableKey + "_" + dKey + " where " + dKey + "_id = "
                    + formData.get("id");
            List<Map<String, Object>> dValue = appService.getInfoBySql(dSql);
            formData.put(dKey, dValue);
        }
        result.put("process", process);
        result.put("formData", formData);
        result.put("tableSchema", formMod.getTableSchema());
        result.put("module", module);
        result.put("tableKey", tableKey);
        result.put("formView", formMod.getFormView());
        result.put("name", formMod.getName());
        result.put("logs", logs);
        result.put("isRequired", isRequired);

        Map<String, Object> paramMap = new HashMap<String, Object>();

        paramMap.put("userId", curUser.getUserId());
        paramMap.put("processId", processId);
        //获取用户日志，用户参与过的节点
        List<OaProcessLog> elementList = oaProcessLogService.getLogEleListByUser(paramMap);
        //获取主表字段idList
        Set<String> formSet = new HashSet<>(oaFormModelService.getMainFormIdList(formMod.getId()));

        //主表隐藏的内容
        Set<String> hideTableList = new HashSet<>();

        int mainFlag = 0;

        List<Map<String, Object>> hideTableDetailList = new ArrayList<>();
        List<Map<String, Object>> showTableDetailList = new ArrayList<>();
        if (elementList.size() != 0) {
            for (OaProcessLog map : elementList) {
                //如果是开始节点，即我是流程发起人，
                if ("startElement".equals(map.getCurNode())) {
                    Map<String, Object> modElementInfo = new HashMap<String, Object>();
                    //主表隐藏的字段为空
                    modElementInfo.put("hideTable", hideTableList);
                    for (String dKey : detailKeyList) {
                        Map<String, Object> keyMap = new HashMap<String, Object>();
                        keyMap.put("hostId", dKey);
                        keyMap.put("orderId", hideTableList);
                        hideTableDetailList.add(keyMap);
                    }
                    //明细表隐藏的字段为空
                    modElementInfo.put("hideTableDetail", hideTableDetailList);
                    result.put("flowFormConfig", modElementInfo);
                    return result;
                }
                HashMap param = new HashMap(2);
                param.put("curNode",map.getCurNode());
                param.put("flowId",map.getFlowId());
                //获取到节点配置
                Map<String, Object> elementConfig = oaFlowModelElementService.getElementConfigByFlowIdAndCurNode(param);
                if (elementConfig == null) {
                    continue;
                }
                Map<String, Object> configMap = JSON.parseObject(elementConfig.get("modConfig").toString(), HashMap.class);
                if (!(!configMap.containsKey("hideTable") && "".equals(configMap.get("hideTable")))) {
                    if (mainFlag == 0) {
                        hideTableList = formSet;
                    }
                    Set<String> tableList = new HashSet<>();
                    Collections.addAll(tableList, configMap.get("hideTable").toString().split(","));
                    hideTableList.retainAll(tableList);
                    mainFlag = 1;
                }

                if (configMap.get("showTableDetail")!=null && ((List<HashMap>)configMap.get("showTableDetail")).size()>0) {
                    List<Map<String, Object>> showTableDetail = (List<Map<String, Object>>) configMap.get("showTableDetail");
                    Map<String, Object> a = new HashMap<String, Object>();
                    for (Map<String, Object> map2 : showTableDetail) {
                        for (String dKey : detailKeyList) {
                            if (map2.get("id").equals(dKey)) {
                                a.put(dKey, map2.get("list"));
                            }
                        }
                    }
                    showTableDetailList.add(a);
                }
            }
        }

        Map<String, Object> tableDetailSch = oaFormModelService.getDetailTableSchema(formMod.getId());
        for (String dKey : detailKeyList) {
            Map<String, Object> keyMap = new HashMap<String, Object>();
            keyMap.put("hostId", dKey);

            Set<String> hideSet = new HashSet<>((List<String>) tableDetailSch.get(dKey));

            for (Map<String, Object> map : showTableDetailList) {
                Set<String> littleShowSet = new HashSet<>((List<String>) map.get(dKey));
                hideSet.removeAll(littleShowSet);
            }
            keyMap.put("orderId", hideSet);
            hideTableDetailList.add(keyMap);
        }
        Map<String, Object> modElementInfo = new HashMap<String, Object>();
        //主表隐藏的字段
        modElementInfo.put("hideTable", hideTableList);
        //明细表隐藏的字段
        modElementInfo.put("hideTableDetail", hideTableDetailList);
        result.put("flowFormConfig", modElementInfo);
        return result;
    }

    @Override
    public Map approval(Map<String, Object> data, JwtModel curUser) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        // 获取前端指定的流程审批人
        List<Map<String, Object>> assigneeList = data.get("assignee") == null ? null
                : (List<Map<String, Object>>) data.get("assignee");
        String json = JSON.toJSONString(assigneeList);
        //系统通知配置
        Map<String,Object> notifyConfig = data.get("notifyConfig")==null?null:(Map<String,Object>)data.get("notifyConfig");
        List<User> assignee = data.get("assignee") == null ? new ArrayList<User>()
                : JSON.parseArray(json, User.class);
        Long curUserId = Long.valueOf(curUser.getUserId());
        //前端穿入的流程状态
        int state = data.get("state") == null ? 0 : (int) data.get("state");
        int exeNum = 0;
        // 流程实例ID
        Long processId = Long.parseLong(data.get("processId").toString());
        Map<String, Object> formData = (Map<String, Object>) data.get("formData");
        OaProcess process = oaProcessService.selectByPrimaryKey(processId);
        if (null == process) {
            throw new Exception("流程存在异常！");
        }
        // 该流程的待审批人还有几个
        HashMap param = new HashMap(2);
        param.put("processId",processId);
        List<OaProcessRun> runList = oaProcessRunService.selectList(param);
        //获取当前审批节点审批人的数量
        int runningCount = runList.size();
        List<Object> runner = new ArrayList<Object>();
        for (OaProcessRun run : runList) {
            //如果当前人事流程审批人，则从待审批人中移除
            if (curUserId.equals(run.getUserId())) {
                continue;
            }
            runner.add(run.getUserId());
        }
        //获取流程的创建人
        Long creatorId = process.getCreateBy();
        // 获取流程所处节点
        String curCode = process.getCode();
        String curCodeName = process.getCodeName();
        Long modId = process.getFlowModelId();
        // 获取节点的配置信息
        Map<String, Object> modConfigMap = oaFlowModelElementService.getNowNodeConfig(curCode,modId);
        //获取当前审批节点的配置信息
        Map<String, Object> modConfig = JSONObject.parseObject((String) modConfigMap.get("modConfig"), Map.class);
        //查看节点配置是否指定了通知人
        String designee = modConfig.get("designee") ==null?null:modConfig.get("designee").toString();
        //查看是否通知发起人
        Boolean initiator = modConfig.get("initiator") ==null?false:(Boolean)modConfig.get("initiator");

//        Map<String, Object> scriptMap = new HashMap<String, Object>();
//        // 执行脚本，目前放在本地启动会存在问题，需打包部署到Tomcat中运行
//        scriptMap = resolveScript((String) modConfigMap.get("modConfig"), formData, appDao);
//        if (null != scriptMap &&null!=scriptMap.get("errCode")&& scriptMap.get("errCode").equals(-1)) {
//            throw new Exception(scriptMap.get("errMsg").toString());
//        }
//        // 脚本中根据需求自定义的异常提示信息，状态码对应是0
//        if (null != scriptMap &&null!=scriptMap.get("errCode")&& scriptMap.get("errCode").equals(0)) {
//            throw new Exception(scriptMap.get("errMsg").toString());
//        }
//        OaFlowModElement nextElement = new OaFlowModElement();
//        if (null != scriptMap && scriptMap.get("nextNode") != null) {
//            QueryWrapper<OaFlowModElement> queryWrapper = new QueryWrapper<>();
//            nextElement = oaFlowModElementDao
//                    .selectOne(queryWrapper.eq("mod_id", modId).eq("code", scriptMap.get("nextNode").toString()));
//            QueryWrapper<OaFlowModConfig> query = new QueryWrapper<>();
//            OaFlowModConfig oaConfig = oaFlowModConfigDao.selectOne(query.eq("mod_element_id", nextElement.getId()));
//            nextElement.setParam(oaConfig.getModConfig());
//            System.err.println("下一节点信息：" + nextElement);
//            // elements.add(nextElement);
//        } else {
//            OaFlowModElement modElement = getNextNodes(curCode, modId, formData, curUserId);
//            if (null == modElement) {
//                throw new Exception("下一节点为空！");
//            }
//            nextElement = modElement;
//        }
        // 获取下一节点
        OaFlowModelElement nextElement = new OaFlowModelElement();
        // 通过当前节点获取下一节点
        OaFlowModelElement modElement = getNextNodes(curCode, modId, formData, curUserId);
        if (null == modElement) {
            throw new Exception("下一节点为空！");
        }
        nextElement = modElement;
        // 排除重复审批人
        Map<String, Object> nextModConfig = oaFlowModelElementService.getNowNodeConfig(nextElement.getCode(),modId);
        Map<String, Object> nextModConfigMap = JSON.parseObject(nextModConfig.get("modConfig").toString(), Map.class);
        //查看下一节点是否排除重复审批人
        Boolean removeDuplicate = nextModConfigMap.get("removeDuplicate") == null ? false
                : (Boolean) nextModConfigMap.get("removeDuplicate");
        Boolean handlerIsEmpty = false;
        // 流程历史参与人（审批人和发起人）
        List<OaProcessLog> logs = oaProcessLogService.selectAll(param);
        Set<User> joiner = new HashSet<User>();
        for (OaProcessLog lg : logs) {
            joiner.addAll(userMapper.getUserInfoByIdStrings(lg.getCurAssignee()));
        }
        joiner.add(userService.selectUserById(curUserId.toString()));
        List<User> hisJoiner = new ArrayList<User>(joiner);
        // 获取下一节点审批人信息
        List<Object> handlerList = new ArrayList<Object>();
        // 下一节点及其名称
        String nextCode = nextElement.getCode();
        String nextCodeName = nextElement.getName();
        //如果还有待审批人
        if (runningCount > 1) {
            nextCode = curCode;
            nextCodeName = curCodeName;
        }
        process.setCode(nextCode);
        process.setCodeName(nextCodeName);
        process.setAssignee(GuavaUtil.list3string(runner, ","));
        // 判断下一节点是否是结束节点
        if ("endElement".equals(nextCode) && runningCount == 1) {
            process.setState(4);
        } else {
            process.setState(state);
        }
        List<User> handlerTemp = new ArrayList<User>();

        if (runningCount == 1) {
            //如果当前节点审批人呢只有一人，当前用户
            Map<String, Object> nextConfigMap = JSONObject.parseObject(nextElement.getParam(), Map.class);
            // 审批人是否多选
            Boolean handlerSelectable = (Boolean) nextConfigMap.get("handlerSelectable");
            // 判断前端是否选择下一节点审批人
            if (null == assignee || assignee.size() == 0) {
                // 解析审批人配置获取审批人
                List<User> handlers = getHandler(nextConfigMap, creatorId);
                handlerTemp = new ArrayList<>(handlers);
                ;
                if ((null == handlers || handlers.isEmpty()) && !("endElement".equals(nextCode))) {
                    throw new Exception("未找到审批人！");
                }
                // 排除重复审批人
                if (removeDuplicate) {
                    //把参与过审批的人都排除掉
                    handlers.removeAll(hisJoiner);
                }
                for (User user : handlers) {
                    handlerList.add(user.getId());
                }
                // 如果有多个审批人并且流程设置审批人可选
                if (handlers.size() > 1 && handlerSelectable) {
                    Map<String, Object> assigneeMap = new HashMap<String, Object>();
                    assigneeMap.put("assignee", handlers);
                    assigneeMap.put("handlerSelectConfig", nextConfigMap.get("handlerSelectConfig"));
                    HashMap temp = new HashMap(1);
                    temp.put("successStatus","2");
                    temp.put("message","请选择审批人");
                    temp.put("data",assigneeMap);
                    return temp;
                } else {
                    if (handlers.size() == 0) {
                        //如果排除过历史审批人之后，没有流程审批人了，则不排除历史审批人
                        handlerIsEmpty = true;
                        assignee = handlerTemp;
                    } else {
                        //如果还有审批人，则排除历史审批人
                        assignee = handlers;
                    }
                    process.setAssignee(GuavaUtil.list3string(handlerList, ","));
                }
                result.put("handlers", handlers);

            } else {
                //如果前端指定了下一审批人
                for (User user : assignee) {
                    handlerList.add(user.getId());
                }
                process.setAssignee(GuavaUtil.list3string(handlerList, ","));
                result.put("handlers", assignee);
            }
        } else {
            //如果当前审批节点审批人有多个
            result.put("handlers", userMapper.getUserInfoByIdStrings(GuavaUtil.list3string(runner, ",")));
        }
        result.put("nodeId", curCode);
        result.put("processId", processId);
        // 更新流程运行状态及信息
        process.setUpdateBy(Long.valueOf(curUser.getUserId()));
        exeNum = oaProcessService.updateByPrimaryKeySelective(process);
        // 更新表单数据
        oaFormModelService.updateFormData(data, curUser);
        OaProcessHis hisProcess = new OaProcessHis();
        hisProcess.setProcessId(process.getId());
        hisProcess.setUserId(curUserId);
        // 将待办流程记录删除
        if (exeNum > 0){
            HashMap paramTwo = new HashMap(2);
            paramTwo.put("processId",processId);
            paramTwo.put("userId",curUserId);
            exeNum = oaProcessRunService.deleteByMap(paramTwo);
        }
        // 添加数据到历史流程处理表中
        if (exeNum > 0){
            hisProcess.setCreateBy(Long.valueOf(curUserId));
            hisProcess.setUpdateBy(Long.valueOf(curUser.getUserId()));
            exeNum = oaProcessHisService.insertSelective(hisProcess);
        }

        for (User handler : assignee) {
            OaProcessRun runProcess = new OaProcessRun();
            runProcess.setProcessId(process.getId());
            runProcess.setUserId(Long.valueOf(handler.getId()));
            // 添加数据到待处理流程表中
            if (exeNum > 0){
                runProcess.setCreateBy(Long.valueOf(curUserId));
                runProcess.setUpdateBy(Long.valueOf(curUser.getUserId()));
                oaProcessRunService.insertSelective(runProcess);
            }
        }
        OaProcessLog processLog = new OaProcessLog();
        processLog.setProcessId(processId);
        processLog.setCurAssignee(curUserId.toString());
        processLog.setCurNode(curCode);
        processLog.setFlowId(process.getFlowId());
        processLog.setNextNode(nextCode);
        if (runningCount == 1) {
            processLog.setNextAssignees(GuavaUtil.list3string(handlerList, ","));
        } else {
            processLog.setNextAssignees(GuavaUtil.list3string(runner, ","));
        }
        processLog.setMessage((String) data.get("message"));
        // 判断下一节点是否是结束节点
        if ("endElement".equals(nextCode) && runningCount == 1) {
            //结束
            processLog.setActionId(5);
        } else {
            //通过
            processLog.setActionId(2);
        }

        // 插入流程日志
        if (exeNum > 0){
            processLog.setCreateBy(Long.valueOf(curUserId));
            processLog.setUpdateBy(Long.valueOf(curUser.getUserId()));
            exeNum = oaProcessLogService.insertSelective(processLog);
        }
        // 自动通过
        if (removeDuplicate && handlerIsEmpty) {
            // 如果排除重复审批人， 审批人为空
            Map<String, Object> handleMap = new HashMap<String, Object>();
            handleMap.put("data", data);
            handleMap.put("handler", handlerTemp);
            handleMap.put("message","自动通过");
            handleMap.put("successStatus","1");
            return handleMap;
        }
        // 数据保存异常进行事务回滚
        if (exeNum <= 0){
            throw new Exception();
        }
        if (runningCount > 1) {
            // 如果当前审批流程有多个审批人
            HashMap temp = new HashMap(1);
            temp.put("message","审批成功"+result);
            return temp;
        }
        Boolean sendNotify = true;
        //流程中自定义通知
        if(null!=notifyConfig) {

        }
        //流程发起过程中配置的系统通知
        result.put("message","审批成功并转移至下一节点"+nextCodeName);
        result.put("successStatus","1");
        return result;
    }

    @Override
    public Map refuse(Map<String, Object> data, JwtModel curUser) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        // 前端参数里规定的返回的节点和审批人
        Map<String, Object> nodeAssignee = data.get("nodeAssignee") == null ? null
                : (Map<String, Object>) data.get("nodeAssignee");
        Long curUserId = Long.valueOf(curUser.getUserId());
        // 前端给的流程状态
        int state = data.get("state") == null ? 0 : (int) data.get("state");
        int exeNum = 0;
        // 流程实例ID
        Long processId = Long.parseLong(data.get("processId").toString());

        OaProcess process = oaProcessService.selectByPrimaryKey(processId);
        if (null == process) {
            HashMap temp = new HashMap(1);
            temp.put("message","流程存在异常");
            temp.put("successStatus","-2");
            return temp;
        }
        Long modId = process.getFlowModelId();

        // 获取流程所处节点
        String curCode = process.getCode();
        // 获取当前节点的配置信息
        Map<String, Object> modConfigMap = oaFlowModelElementService.getNowNodeConfig(curCode, modId);
        // 节点配置
        Map<String, Object> configMap = JSONObject.parseObject(modConfigMap.get("modConfig").toString(), Map.class);
        // 节点是否配置返回节点可选
        Boolean isShowBackLsit = configMap.get("isShowBackLsit") == null ? false
                : (boolean) configMap.get("isShowBackLsit");
        if (isShowBackLsit && nodeAssignee == null) {
            List<Map<String, Object>> nodes = oaFlowModelElementService.getNodeByProcessId(processId);
            HashMap temp = new HashMap(1);
            temp.put("message","请选择回退节点");
            temp.put("successStatus","2");
            temp.put("data",nodes);
            return temp;
        }
        // 获取上一节点审批人信息
        List<User> handlers = new ArrayList<User>();
        List<Object> handlerList = new ArrayList<Object>();
        // 下一节点及其名称
        String lastCode = null;
        String lastCodeName = null;
        //如果勾选了退回人可选，并且前端也传了回退人
        if (isShowBackLsit && null != nodeAssignee) {
            //获取当前审批节点审批人
            String assignee = (String) nodeAssignee.get("cur_assignee");
            List<User> handler = userMapper.getUserInfoByIdStrings(assignee);
            lastCode = (String) nodeAssignee.get("code");
            lastCodeName = (String) nodeAssignee.get("name");
            process.setCode(lastCode);
            process.setCodeName(lastCodeName);
            // 设置已拒绝状态码1,待重审状态码2
            if ("startElement".equals(lastCode)) {
                //如果上一节点是开始节点，状态为拒绝
                process.setState(1);
            } else {
                //如果上一节点不是开始节点，状态为待重审
                process.setState(2);
            }
            // 更新流程运行状态及信息
            // 从前端传入的回退人中选一个为待重审人员
            process.setAssignee(handler.get(0).getId().toString());
            process.setUpdateBy(Long.valueOf(curUser.getUserId()));
            exeNum = oaProcessService.updateByPrimaryKeySelective(process);
            handlers.addAll(handler);
            result.put("handlers", handlers);
            result.put("nodeId", lastCode);
            result.put("processId", processId);
        } else {
            // 如果前端没规定退回节点可选，
            // 获取上一节点
            // 因为上一个节点可能有多个审批人，（日志中的当前节点和下一节点不一致）
            HashMap param = new HashMap(3);
            param.put("processId",processId);
            param.put("nextNode",curCode);
            param.put("noCurNode",curCode);
            List<OaProcessLog> processLogList = oaProcessLogService.selectAll(param);
            List<User> handler = new ArrayList<User>();
            for (OaProcessLog oaProcessLog : processLogList) {
                handler.add(userService.selectUserById(oaProcessLog.getCurAssignee()));
                // 上一节点名称应一致
                lastCode = oaProcessLog.getCurNode();
            }
            // 根据流程模型ID和节点代码获取节点信息
            OaFlowModelElement element = oaFlowModelElementService.selectOneByCodeAndModelId(lastCode,modId);
            lastCodeName = element.getName();
            process.setCode(lastCode);
            process.setCodeName(lastCodeName);
            // 设置已拒绝状态码1,待重审状态码2
            if ("startElement".equals(lastCode)) {
                process.setState(1);
            } else {
                process.setState(2);
            }
            // 更新流程运行状态及信息
            process.setAssignee(handler.get(0).getId().toString());
            process.setUpdateBy(Long.valueOf(curUser.getUserId()));
            exeNum = oaProcessService.updateByPrimaryKeySelective(process);
            handlers.addAll(handler);
            result.put("handlers", handlers);
            result.put("nodeId", lastCode);
            result.put("processId", processId);
        }
        // 更新表单数据
        oaFormModelService.updateFormData(data, curUser);
        // 添加历史流程
        OaProcessHis hisProcess = new OaProcessHis();
        hisProcess.setProcessId(process.getId());
        hisProcess.setUserId(curUserId);
        // 添加数据到历史流程处理表中
        if (exeNum > 0){
            hisProcess.setCreateBy(Long.valueOf(curUser.getUserId()));
            hisProcess.setUpdateBy(Long.valueOf(curUser.getUserId()));
            exeNum = oaProcessHisService.insertSelective(hisProcess);
        }
        for (User handler : handlers) {
            OaProcessRun runProcess = new OaProcessRun();
            runProcess.setProcessId(process.getId());
            if (exeNum > 0){
                exeNum = oaProcessRunService.deleteByProcessId(process.getId());
            }
            runProcess.setUserId(Long.valueOf(handler.getId()));
            handlerList.add(handler.getId());
            // 添加数据到待处理流程表中
            if (exeNum > 0){
                runProcess.setCreateBy(Long.valueOf(curUser.getUserId()));
                runProcess.setUpdateBy(Long.valueOf(curUser.getUserId()));
                exeNum = oaProcessRunService.insertSelective(runProcess);
            }
        }
        OaProcessLog processLog = new OaProcessLog();
        processLog.setProcessId(processId);
        processLog.setCurAssignee(curUserId.toString());
        processLog.setCurNode(curCode);
        processLog.setFlowId(process.getFlowId());
        processLog.setNextNode(lastCode);
        processLog.setNextAssignees(GuavaUtil.list3string(handlerList, ","));
        processLog.setMessage((String) data.get("message"));
        processLog.setActionId(3);
        // 插入流程日志
        if (exeNum > 0){
            processLog.setCreateBy(Long.valueOf(curUser.getUserId()));
            processLog.setUpdateBy(Long.valueOf(curUser.getUserId()));
            exeNum = oaProcessLogService.insertSelective(processLog);
        }
        // 数据保存异常进行事务回滚
        if (exeNum <= 0){
            throw new Exception();
        }
        result.put("message","审批成功并转移至下一节点:" + lastCodeName);
        result.put("successStatus","1");
        return result;
    }

    @Override
    public Map forward(Map<String, Object> data, JwtModel sysUser) throws Exception {
        // TODO Auto-generated method stub
        Map<String, Object> result = new HashMap<String, Object>();
        Long curUserId = Long.valueOf(sysUser.getUserId());
        //转交人员userId
        Long transferId = Long.parseLong(data.get("transferId").toString());
        if (transferId.equals(curUserId)) {
            result.put("successStatus","-2");
            result.put("message","转交人不能为本人");
            return result;
        }
        // 数据库操作状态码
        int exeNum = 1;
        // 流程实例ID
        Long processId = Long.parseLong(data.get("processId").toString());

        OaProcess process = oaProcessService.selectByPrimaryKey(processId);
        if (null == process) {
            result.put("successStatus","-3");
            result.put("message","流程存在异常");
            return result;
        }
        // 获取流程所处节点
        String curCode = process.getCode();


        // 获取下一节点审批人信息
        List<User> handlers = new ArrayList<User>();
        List<Object> handlerList = new ArrayList<Object>();

        OaProcessHis hisProcess = new OaProcessHis();
        hisProcess.setProcessId(process.getId());
        hisProcess.setUserId(curUserId);
        //将当前处理人处理信息加入流程处理历史
        exeNum = oaProcessHisService.insertSelective(hisProcess);

        handlers.add(userService.selectUserById(transferId.toString()));

        result.put("handlers", handlers);
        result.put("nodeId", curCode);
        result.put("processId", processId);
        // 更新表单数据
        oaFormModelService.updateFormData(data, sysUser);

        for (User handler : handlers) {
            OaProcessRun runProcess = new OaProcessRun();
            runProcess.setProcessId(process.getId());
            runProcess.setUserId(transferId);
            if (exeNum > 0) {
                //将转交人加入到流程待办中
                exeNum = oaProcessRunService.insertSelective(runProcess);
            }
            handlerList.add(handler.getId());
            // 添加数据到待处理流程表中
        }
        OaProcessLog processLog = new OaProcessLog();
        processLog.setProcessId(processId);
        processLog.setCurAssignee(curUserId.toString());
        processLog.setCurNode(curCode);
        processLog.setFlowId(process.getFlowId());
        processLog.setNextNode(curCode);
        processLog.setNextAssignees(GuavaUtil.list3string(handlerList, ","));
        processLog.setMessage((String) data.get("message"));
        // 将日志状态标记为转交
        processLog.setActionId(4);

        // 将当前人待办流程记录删除
        if (exeNum > 0){
            Map param = new HashMap();
            param.put("processId",processId);
            param.put("userId",curUserId);
            exeNum = oaProcessRunService.deleteByMap(param);
        }

        // 插入流程日志
        if (exeNum > 0){
            exeNum = oaProcessLogService.insertSelective(processLog);
        }
        // 数据保存异常进行事务回滚
        if (exeNum <= 0){
            throw new Exception();
        }
        result.put("successStatus","1");
        return result;
    }

    @Override
    public int deleteProcess(Long processId) throws Exception {
        OaProcess oaProcess = oaProcessService.selectByPrimaryKey(processId);
        if (null == oaProcess) {
            throw new Exception("该流程不存在！");
        }
        Long appFormId = oaProcess.getAppFormId();
        //流程模型ID
        Long flowModId = oaProcess.getFlowModelId();
        //流程表单绑定关系
        OaFlowForm flowForm = oaFlowFormService.selectByFlowModelId(flowModId);
        if (null == flowForm) {
            throw new Exception("流程和表单绑定存在异常，请联系开发人员！");
        }
        OaFormModel formMod = oaFormModelService.selectByPrimaryKey(flowForm.getFormModelId());
        if (null == formMod) {
            throw new Exception("该流程表单模型存在异常，请联系开发人员！");
        }
        String tableKey = formMod.getTableKey();
        String detailKeys = formMod.getDetailKeys();
        Boolean flag = true;
        // 删除主表信息
        if (null != tableKey && !tableKey.isEmpty()) {
            String sql = "delete from _app_" + tableKey + " where id = " + appFormId;
            try {
                appService.CUD(sql);
                flag = true;
            } catch (Exception e) {
                flag = false;
            }
        }
        // 删除明细表关联信息
        if (null != detailKeys && !detailKeys.isEmpty()) {
            List<String> dKeys = GuavaUtil.split2list(",", detailKeys);
            for (String dKey : dKeys) {
                String sql = "delete from _app_" + tableKey + "_" + dKey + " where " + dKey + "_id = " + appFormId;
                try {
                    appService.CUD(sql);
                    flag = true;
                } catch (Exception e) {
                    flag = false;
                }
            }
        }
        int exeNum = 0;

        // 删除流程实例
        exeNum = oaProcessService.deleteByPrimaryKey(processId);
        // 删除历史流程
        if (exeNum > 0) {
            oaProcessHisService.deleteByProcessId(processId);
        }
        // 删除待办流程
        if (exeNum > 0) {
            oaProcessRunService.deleteByProcessId(processId);
        }
        // 删除流程日志
        if (exeNum > 0) {
            oaProcessLogService.deleteByProcessId(processId);
        }
        return exeNum;
    }


}
