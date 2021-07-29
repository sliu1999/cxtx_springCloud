package com.cxtx.user_manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.cxtx.common.domain.JwtModel;
import com.cxtx.common.unit.HttpServletUtils;
import com.cxtx.common.unit.ServiceUtil;
import com.cxtx.user_manage.domain.*;
import com.cxtx.user_manage.mapper.*;
import com.cxtx.user_manage.service.OaFlowModelDetailService;
import com.cxtx.user_manage.service.OaFlowModelElementService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.service.OaFlowService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Transactional(rollbackFor = Exception.class)
public class OaFlowServiceImpl implements OaFlowService {


    @Autowired
    private OaFlowMapper oaFlowMapper;


    @Autowired
    private OaFlowModelMapper oaFlowModelMapper;

    @Autowired
    private OaFlowModelElementMapper oaFlowModelElementMapper;
    @Autowired
    private OaFlowModelDetailMapper oaFlowModelDetailMapper;
    @Autowired
    private OaFlowModelElementConfigMapper oaFlowModelElementConfigMapper;
    @Autowired
    private OaFlowFormMapper oaFlowFormMapper;
    @Autowired
    private OaFormModelMapper oaFormModelMapper;

    @Autowired
    private OaFlowModelDetailService oaFlowModelDetailService;

    @Autowired
    private OaFlowModelElementService oaFlowModelElementService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByPrimaryKey(Long id) throws Exception{
        OaFlowModel oaFlowModel = oaFlowModelMapper.selectByFlowId(id);
        //删除模型详情
        oaFlowModelDetailMapper.deleteByModelId(oaFlowModel.getId());

        // 删除模型元素及配置
        Map<String, Object> columnMap = new HashMap<String, Object>();
        columnMap.put("modelId", oaFlowModel.getId());
        List<OaFlowModelElement> elements = oaFlowModelElementMapper.selectAll(columnMap);
        List<Long> elementList = new ArrayList<>();
        for (OaFlowModelElement element : elements) {
            elementList.add(element.getId());
        }
        oaFlowModelElementConfigMapper.deleteByElementList(elementList);
        oaFlowModelElementMapper.deleteByModelId(oaFlowModel.getId());
        //删除流程表单关联
        oaFlowFormMapper.deleteByFlowId(id);
        //删除流程模型
        oaFlowModelMapper.deleteByFlowId(id);
        //删除流程
        int result = oaFlowMapper.deleteByPrimaryKey(id);

        if (result <= 0) {
            throw new Exception();
        }
        return result;
    }

    @Override
    public int insertSelective(OaFlow record) {
        return oaFlowMapper.insertSelective(record);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public OaFlow selectByPrimaryKey(Long id) {
        OaFlow oaFlow = oaFlowMapper.selectByPrimaryKey(id);
        OaFlowModel oaFlowModel = oaFlowModelMapper.selectByFlowId(id);
        if (null == oaFlowModel) {
            return null;
        }
        HashMap param = new HashMap(1);
        param.put("modelId",oaFlowModel.getId());
        List<OaFlowModelDetail> oaFlowModelDetails = oaFlowModelDetailMapper.selectAll(param);
        List<OaFlowModelElement> oaFlowModelElementList = oaFlowModelElementMapper.selectAll(param);
        for (OaFlowModelElement oaFlowModelElement:oaFlowModelElementList){
            OaFlowModelElementConfig oaFlowModelElementConfig = oaFlowModelElementConfigMapper.selectByElementId(oaFlowModelElement.getId());
            if(oaFlowModelElementConfig != null){
                oaFlowModelElement.setModElementConfig((Map<String, Object>) JSON.parse(oaFlowModelElementConfig.getElementConfig()));
            }
        }
        Map<String, Object> modMap = new HashMap<String, Object>();
        for (OaFlowModelDetail dd : oaFlowModelDetails) {
            modMap.put(dd.getNodeCode(), dd);
        }
        Set<Map<String, Object>> result = new HashSet<Map<String, Object>>();
        for (OaFlowModelDetail dd : oaFlowModelDetails) {
            Map<String, Object> element = new HashMap<String, Object>();
            String nextNodeCode = dd.getNextNodeCode();
            // 将节点的关系转换成线的指向关系
            if (nextNodeCode != null) {
                element.put("fromNodeCode", dd.getNodeCode());
                element.put("toNodeCode", nextNodeCode);
                result.add(element);
            }
        }
        List<Map<String, Object>> sequenceElements = new ArrayList<>(result);
        Map<String, Object> module = new HashMap<String, Object>();

        //根据流程模型ID查询流程绑定的表单关联信息
        OaFlowForm flowForm = oaFlowFormMapper.selectByFlowModelId(oaFlowModel.getId());
        //根据表单模型ID查询表单模型信息
        OaFormModel formMod = oaFormModelMapper.selectByPrimaryKey(flowForm.getFormModelId());
        module.put("tableKey", formMod.getTableKey());
        module.put("catId", formMod.getFormType());
        module.put("id", formMod.getId());
        module.put("name", formMod.getName());
        oaFlow.setModule(module);
        // 流程模型
        oaFlow.setProcessModel(oaFlowModel);
        // 节点之间的关联关系
        oaFlow.setModSequenceElements(sequenceElements);
        // 节点集合
        oaFlow.setModElements(oaFlowModelElementList);
        return oaFlow;
    }

    @Override
    public int updateByPrimaryKeySelective(OaFlow record) {
        return oaFlowMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<OaFlow> selectAll(Map params) {
        return oaFlowMapper.selectAll(params);
    }

    @Override
    public PageInfo<OaFlow> queryByPage(Map params) {
        ServiceUtil.checkPageParams(params);
        PageHelper.startPage(params);
        List<OaFlow> labels = oaFlowMapper.selectAll(params);
        return new PageInfo<OaFlow>(labels);
    }
    @Override
    public OaFlow newFindFlow(Long flowId, Date processDate) {
        JwtModel curUser = HttpServletUtils.getUserInfo();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 根据流程ID查询流程
        OaFlow flow = selectByPrimaryKey(flowId);

        // 根据流程ID和创建时间获取流程信息
        HashMap param = new HashMap(2);
        param.put("flowId",flow.getId());
        param.put("createDate",sdf.format(processDate));
        List<OaFlowModel> mods = oaFlowModelMapper.selectAll(param);
        List<Date> createDates = new ArrayList<>();
        for (OaFlowModel oaFlowMod : mods) {
            createDates.add(oaFlowMod.getCreateDate());
        }
        Date max = createDates.get(0);
        for (int i = 0; i < createDates.size(); i++) {
            if (max.compareTo(createDates.get(i)) < 0) {
                max = createDates.get(i);
            }
        }
        // 找到离流程发起前最近的那一版流程模板
        OaFlowModel mod = oaFlowModelMapper.selectByCreateDate(max);

        if (null == mod) {
            return null;
        }
        Long modId = mod.getId();
        // 根据流程ID获取流程节点详情
        HashMap param2 = new HashMap(1);
        param2.put("modelId",modId);
        List<OaFlowModelDetail> modDetail = oaFlowModelDetailService.selectAll(param2);
        // 根据模型ID获取流程模型元素集合
        List<OaFlowModelElement> modElement = oaFlowModelElementService.selectAll(param2);
        for (OaFlowModelElement element : modElement) {
            OaFlowModelElementConfig config = oaFlowModelElementConfigMapper.selectByElementId(element.getId());
            if (null != config){
                element.setModElementConfig((Map<String, Object>) JSON.parse(config.getElementConfig()));
            }

        }
        Map<String, Object> modMap = new HashMap<String, Object>();
        for (OaFlowModelDetail dd : modDetail) {
            modMap.put(dd.getNodeCode(), dd);
        }

        Set<Map<String, Object>> result = new HashSet<Map<String, Object>>();
        for (OaFlowModelDetail dd : modDetail) {
            Map<String, Object> element = new HashMap<String, Object>();
            String nextNodeCode = dd.getNextNodeCode();
            // 将节点的关系转换成线的指向关系
            if (nextNodeCode != null) {
                element.put("fromNodeCode", dd.getNodeCode());
                element.put("toNodeCode", nextNodeCode);
                result.add(element);
            }
        }
        List<Map<String, Object>> sequenceElements = new ArrayList<>(result);
        Map<String, Object> module = new HashMap<String, Object>();
        //根据流程ID查找最新版本的流程模型
        OaFlowModel flowMod = oaFlowModelMapper.selectByFlowId(flowId);
        //根据流程模型ID查询流程绑定的表单关联信息
        OaFlowForm flowForm = oaFlowFormMapper.selectByFlowModelId(flowMod.getId());
        //根据表单模型ID查询表单模型信息
        OaFormModel formMod = oaFormModelMapper.selectByPrimaryKey(flowForm.getFormModelId());
        module.put("tableKey", formMod.getTableKey());
        module.put("catId", formMod.getFormType());
        module.put("id", formMod.getId());
        module.put("name", formMod.getName());
        flow.setModule(module);
        // 流程模型
        flow.setProcessModel(mod);
        // 节点之间的关联关系
        flow.setModSequenceElements(sequenceElements);
        // 节点集合
        flow.setModElements(modElement);

        return flow;
    }


    /**
     * 先判断是新增，还是编辑
     * 1，如果新增先查看是否存在相同流程名，如果存在，则返回，如果不存在，则新增
     * 2，如果是编辑则更新
     * 3, 如果是编辑，则删除老的流程模型及模型详情，元素及配置
     * 3，如果未绑定表单，则抛出异常，回滚
     * 4，保存新的流程模型
     * 5，删除老的流程表单关联，保存新的流程表单关联
     * 6，新增新的流程模型元素及元素配置
     * 7，新增新的流程模型详情
     * @param oaFlow
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map addOrEditFlow(OaFlow oaFlow) throws Exception {
        JwtModel curUser = HttpServletUtils.getUserInfo();
        // 初始化参数
        int exeNum = 0;

        // 流程模型元素
        List<OaFlowModelElement> modElements = oaFlow.getModElements();
        // 流程模型明细
        List<Map<String, Object>> modSequenceElements = oaFlow.getModSequenceElements();
        //表单模型信息
        Map<String, Object> module = oaFlow.getModule();

        // 新增流程名字不能重复
        HashMap param = new HashMap(2);
        param.put("status","1");
        param.put("flowName",oaFlow.getFlowName());
        //判断新增时此流程是否已存在
        OaFlow flow = oaFlowMapper.selectOne(param);
        if (null != flow && null == oaFlow.getId()) {
            //流程名重复
            return null;
        }
        // 判断前端操作是不是对流程进行编辑
        flow = oaFlowMapper.selectByPrimaryKey(oaFlow.getId());

        oaFlow.setUpdateBy(Long.valueOf(curUser.getUserId()));
        if (null != flow) {
            // 流程已存在
            exeNum = oaFlowMapper.updateByPrimaryKeySelective(oaFlow);
        } else {
            // 保存流程
            oaFlow.setCreateBy(Long.valueOf(curUser.getUserId()));
            exeNum = oaFlowMapper.insertSelective(oaFlow);
        }
        // 赋值流程ID
        Long flowId = oaFlow.getId();
        if (null == module.get("id") || module.get("id").toString().isEmpty()) {
            //未绑定表单
            throw new Exception("未绑定表单！");
        }
        // 流程模型，对前台传参进行非空判断
        OaFlowModel oaFlowMod = oaFlow.getProcessModel() == null ? new OaFlowModel() : oaFlow.getProcessModel();
        //判读这个流程模型是否已存在
        OaFlowModel oaFlowModel = oaFlowModelMapper.selectByFlowId(flowId);
        //如果是更新，则删除老的模型及模型相关数据
        if(oaFlowModel!=null && exeNum > 0){
            //获取老的流程模型元素,及元素配置
            HashMap paramTwo = new HashMap(1);
            paramTwo.put("modelId",oaFlowModel.getId());
            List<OaFlowModelElement> oaFlowModelElementList = oaFlowModelElementMapper.selectAll(paramTwo);
            if(oaFlowModelElementList != null){
                List<Long> elementIds = new ArrayList<>(1);
                for(OaFlowModelElement oaFlowModelElement:oaFlowModelElementList){
                    elementIds.add(oaFlowModelElement.getId());
                    oaFlowModelElementMapper.deleteByPrimaryKey(oaFlowModelElement.getId());
                }
                oaFlowModelElementConfigMapper.deleteByElementList(elementIds);
            }
            //删除老的模型详情
            oaFlowModelDetailMapper.deleteByModelId(oaFlowModel.getId());
            //删除老的模型
            oaFlowModelMapper.deleteByFlowId(flowId);
        }
        //重置ID
        oaFlowMod.setId(null);
        oaFlowMod.setFlowId(flowId);
        oaFlowMod.setFlowName(oaFlow.getFlowName());
        // 保存新的流程模型
        if (exeNum > 0){
            oaFlowMod.setCreateBy(Long.valueOf(curUser.getUserId()));
            oaFlowMod.setUpdateBy(Long.valueOf(curUser.getUserId()));
            exeNum = oaFlowModelMapper.insertSelective(oaFlowMod);
        }

        // 赋值流程模型ID
        Long flowModId = oaFlowMod.getId();
        // 表单模型ID
        Long formModId = Long.parseLong(module.get("id").toString());
        //删除flowFromModel表中flow的相关数据
        oaFlowFormMapper.deleteByFlowId(flowId);


        //新增数据
        OaFlowForm flowForm = new OaFlowForm();
        flowForm.setFlowId(flowId);
        flowForm.setFormModelId(formModId);
        flowForm.setStatus(1);
        flowForm.setFlowModelId(flowModId);
        if (exeNum > 0){
            flowForm.setCreateBy(Long.valueOf(curUser.getUserId()));
            flowForm.setUpdateBy(Long.valueOf(curUser.getUserId()));
            exeNum = oaFlowFormMapper.insertSelective(flowForm);
        }


        // 保存流程模型元素及元素配置
        for (OaFlowModelElement modElement : modElements) {
            if (exeNum > 0) {
                Map<String, Object> modElementConfig = modElement.getModElementConfig();
                OaFlowModelElementConfig modConfig = new OaFlowModelElementConfig();
                modConfig.setElementConfig(JSON.toJSONString(modElementConfig));
                modElement.setId(null);
                modElement.setModelId(flowModId);
                modElement.setCreateBy(Long.valueOf(curUser.getUserId()));
                modElement.setUpdateBy(Long.valueOf(curUser.getUserId()));
                exeNum = oaFlowModelElementMapper.insertSelective(modElement);
                if (null != modElementConfig && !modElementConfig.isEmpty()) {
                    modConfig.setElementId(modElement.getId());
                    if (exeNum > 0){
                        modConfig.setCreateBy(Long.valueOf(curUser.getUserId()));
                        modConfig.setUpdateBy(Long.valueOf(curUser.getUserId()));
                        exeNum = oaFlowModelElementConfigMapper.insertSelective(modConfig);
                    }

                }
            }
        }

        // 将线的关系转换成点的关系
        Set<OaFlowModelDetail> details = new HashSet<OaFlowModelDetail>();
        for (Map<String, Object> sequence : modSequenceElements) {
            OaFlowModelDetail detail = new OaFlowModelDetail();
            detail.setModelId(flowModId);
            // 开始节点
            String fromCode = sequence.get("fromNodeCode").toString();
            // 指向节点
            String toCode = sequence.get("toNodeCode").toString();
// 来自开始节点，进来一次，链的头
            if ("startElement".equals(fromCode)) {
                //开始节点加一次详情，当前节点和下一节点
                detail.setNodeCode(fromCode);
                detail.setNextNodeCode(toCode);
                if (exeNum > 0){
                    details.add(detail);
                }
                continue;
            }
// 下一节点是结束节点，新增两条数据
            if ("endElement".equals(toCode)) {
                //设置当前节点和上一节点
//                detail.setNodeCode(toCode);
//                detail.setLastNodeCode(fromCode);
//                if (exeNum > 0){
//                    //第一次新增
//                    details.add(detail);
//                }
                for (Map<String, Object> oaSequence : modSequenceElements) {

                    if (fromCode.equals(oaSequence.get("toNodeCode"))) {
                        detail.setNodeCode(fromCode);
                        detail.setNextNodeCode(toCode);
                        detail.setLastNodeCode(oaSequence.get("fromNodeCode").toString());
                        if (exeNum > 0){
                            details.add(detail);
                        }
                    }
                    continue;
                }
                continue;
            }
//审批节点设置当前节点和下一节点
            detail.setNodeCode(fromCode);
            detail.setNextNodeCode(toCode);
            for (Map<String, Object> oaSequence : modSequenceElements) {
                if (fromCode.equals(oaSequence.get("toNodeCode"))) {
                    detail.setLastNodeCode(oaSequence.get("fromNodeCode").toString());
                    if (exeNum > 0){
                        details.add(detail);
                    }
                }
                continue;
            }
        }
        for (OaFlowModelDetail dd : details) {
            if (exeNum > 0){
                dd.setCreateBy(Long.valueOf(curUser.getUserId()));
                dd.setUpdateBy(Long.valueOf(curUser.getUserId()));
                exeNum = oaFlowModelDetailMapper.insertSelective(dd);
            }

        }

        // 操作存在异常抛出，进行事务回滚
        if (exeNum <= 0) {
            throw new Exception("系统异常！");
        }
        Map<String, Object> flowMap = new HashMap<String, Object>();
        flowMap.put("id", flowId);
        return exeNum > 0 ? flowMap : null;
    }

    @Override
    public OaFlow selectFlowByForm(Long formId) {
        return oaFlowMapper.selectFlowByForm(formId);
    }


}
