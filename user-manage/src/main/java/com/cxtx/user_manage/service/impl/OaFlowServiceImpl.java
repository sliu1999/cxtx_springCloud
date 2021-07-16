package com.cxtx.user_manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.cxtx.common.unit.ServiceUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaFlow;
import com.cxtx.user_manage.mapper.OaFlowMapper;
import com.cxtx.user_manage.service.OaFlowService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;

/**
 * OaFlow业务逻辑实现类
 * <p>
 * generated by Handsmap New Technology R&D Center  on 2021-7-16 9:22:26
 * create by sliu
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OaFlowServiceImpl implements OaFlowService {


    @Autowired
    private OaFlowMapper oaFlowMapper;


    @Override
    public int deleteByPrimaryKey(Long id) {
        return oaFlowMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(OaFlow record) {
        return oaFlowMapper.insertSelective(record);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public OaFlow selectByPrimaryKey(Long id) {
        return oaFlowMapper.selectByPrimaryKey(id);
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

    @Resource
    OaFlowDao oaFlowDao;

    @Resource
    OaFlowModDao oaFlowModDao;

    @Resource
    OaFlowElementDao oaFlowElementDao;
    @Resource
    OaFlowModElementDao oaFlowModElementDao;
    @Resource
    OaFlowModDetailDao oaFlowModDetailDao;
    @Resource
    OaFlowModConfigDao oaFlowModConfigDao;
    @Resource
    OaFlowFormDao oaFlowFormDao;
    @Resource
    OaFormModDao oaFormModDao;

    @Override
    public boolean saveFlow(OaFlow oaFlow) {
        QueryWrapper<OaFlow> queryWrapper = new QueryWrapper<OaFlow>(oaFlow);
        List<OaFlow> selectList = oaFlowDao.selectList(queryWrapper);
        if (selectList.size() > 0) {
            return false;
        }
        oaFlowDao.insert(oaFlow);
        return true;
    }

    @Override
    @SuppressWarnings("all")
    public List getFlowTypeByModName(String modType) {
        List returnList = new ArrayList<>();
        // List<OaFlow> oaFlowList = oaFlowDao.selectList(new
        // QueryWrapper<OaFlow>().eq("mod_type", modType).orderByDesc("create_date"));
        List<OaFlowMod> oaFlowModList = oaFlowModDao
                .selectList(new QueryWrapper<OaFlowMod>().eq("mod_type", modType).orderByDesc("create_date"));
        // returnList.add(oaFlowList.subList(0, 1));
        returnList.add(oaFlowModList.subList(0, 1));
        return returnList;
    }

    @Override
    public boolean editFlow(@Valid OaFlow oaFlow) {
        oaFlowDao.delete(new QueryWrapper<OaFlow>().eq("flow_name", oaFlow.getFlowName()));
        int insert = oaFlowDao.insert(oaFlow);
        if (insert != 0) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public OaFlowDTO findFlow(Long flowId) {

        // 根据流程ID查询流程
        OaFlow flow = oaFlowDao.selectById(flowId);
        OaFlowDTO flowDto = new OaFlowDTO();
        BeanUtils.copyProperties(flow, flowDto);
        QueryWrapper<OaFlowMod> wrapper = new QueryWrapper<>();
        // 根据流程ID和流程版本号获取流程信息
        OaFlowMod mod = oaFlowModDao
                .selectOne(wrapper.eq("flow_id", flow.getId()).orderByDesc("mod_version").last("limit 1"));
        if (null == mod) {
            return null;
        }
        Long modId = mod.getId();
        // 根据流程ID获取流程节点详情
        QueryWrapper<OaFlowModDetail> queryWrapper = new QueryWrapper<OaFlowModDetail>();
        List<OaFlowModDetail> modDetail = oaFlowModDetailDao.selectList(queryWrapper.eq("mod_id", modId));
        QueryWrapper<OaFlowModElement> elementWrapper = new QueryWrapper<OaFlowModElement>();
        // 根据模型ID获取流程模型元素集合
        List<OaFlowModElement> modElement = oaFlowModElementDao.selectList(elementWrapper.eq("mod_id", modId));
        for (OaFlowModElement element : modElement) {
            QueryWrapper<OaFlowModConfig> configWrapper = new QueryWrapper<>();
            OaFlowModConfig config = oaFlowModConfigDao.selectOne(configWrapper.eq("mod_element_id", element.getId()));
            if (null != config)
                element.setModElementConfig((Map<String, Object>) JSON.parse(config.getModConfig()));
        }
        Map<String, Object> modMap = new HashMap<String, Object>();
        for (OaFlowModDetail dd : modDetail) {
            modMap.put(dd.getNodeCode(), dd);
        }
        Set<Map<String, Object>> result = new HashSet<Map<String, Object>>();
        for (OaFlowModDetail dd : modDetail) {
            // OaFlowSequence element = new OaFlowSequence();
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
        OaFlowMod flowMod = oaFlowModDao.selectByFlowId(flowId);
        //根据流程模型ID查询流程绑定的表单关联信息
        OaFlowForm flowForm = oaFlowFormDao.selectOne(new QueryWrapper<OaFlowForm>().eq("flow_mod_id", flowMod.getId()));
        //根据表单模型ID查询表单模型信息
        OaFormMod formMod = oaFormModDao.selectById(flowForm.getFormModId());
        module.put("tableKey", formMod.getTableKey());
        module.put("catId", formMod.getTypeId());
        module.put("id", formMod.getId());
        module.put("name", formMod.getName());
        flowDto.setModule(module);
        // 流程模型
        flowDto.setProcessModel(mod);
        // 节点之间的关联关系
        flowDto.setModSequenceElements(sequenceElements);
        // 节点集合
        flowDto.setModElements(modElement);

        return flowDto;
    }

    @SuppressWarnings("unchecked")
    @Override
    public OaFlowDTO newFindFlow(Long flowId, Date processDate) {

        // 根据流程ID查询流程
        OaFlow flow = oaFlowDao.selectById(flowId);
        OaFlowDTO flowDto = new OaFlowDTO();
        BeanUtils.copyProperties(flow, flowDto);
        QueryWrapper<OaFlowMod> wrapper = new QueryWrapper<>();
        // 根据流程ID和创建时间获取流程信息
        List<OaFlowMod> mods = oaFlowModDao
                .selectList(wrapper.eq("flow_id", flow.getId()).le("create_date", processDate));
        List<Date> createDates = new ArrayList<>();
        for (OaFlowMod oaFlowMod : mods) {
            createDates.add(oaFlowMod.getCreateDate());
        }
        Date max = createDates.get(0);
        for (int i = 0; i < createDates.size(); i++) {
            if (max.compareTo(createDates.get(i)) < 0) {
                max = createDates.get(i);
            }
        }
        // 找到离流程发起前最近的那一版流程模板
        OaFlowMod mod = oaFlowModDao.selectOne(new QueryWrapper<OaFlowMod>().eq("create_date", max));

        if (null == mod) {
            return null;
        }
        Long modId = mod.getId();
        // 根据流程ID获取流程节点详情
        QueryWrapper<OaFlowModDetail> queryWrapper = new QueryWrapper<OaFlowModDetail>();
        List<OaFlowModDetail> modDetail = oaFlowModDetailDao.selectList(queryWrapper.eq("mod_id", modId));
        QueryWrapper<OaFlowModElement> elementWrapper = new QueryWrapper<OaFlowModElement>();
        // 根据模型ID获取流程模型元素集合
        List<OaFlowModElement> modElement = oaFlowModElementDao.selectList(elementWrapper.eq("mod_id", modId));
        for (OaFlowModElement element : modElement) {
            QueryWrapper<OaFlowModConfig> configWrapper = new QueryWrapper<>();
            OaFlowModConfig config = oaFlowModConfigDao.selectOne(configWrapper.eq("mod_element_id", element.getId()));
            if (null != config)
                element.setModElementConfig((Map<String, Object>) JSON.parse(config.getModConfig()));
        }
        Map<String, Object> modMap = new HashMap<String, Object>();
        for (OaFlowModDetail dd : modDetail) {
            modMap.put(dd.getNodeCode(), dd);
        }

        Set<Map<String, Object>> result = new HashSet<Map<String, Object>>();
        for (OaFlowModDetail dd : modDetail) {
            // OaFlowSequence element = new OaFlowSequence();
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
        OaFlowMod flowMod = oaFlowModDao.selectByFlowId(flowId);
        //根据流程模型ID查询流程绑定的表单关联信息
        OaFlowForm flowForm = oaFlowFormDao.selectOne(new QueryWrapper<OaFlowForm>().eq("flow_mod_id", flowMod.getId()));
        //根据表单模型ID查询表单模型信息
        OaFormMod formMod = oaFormModDao.selectById(flowForm.getFormModId());
        module.put("tableKey", formMod.getTableKey());
        module.put("catId", formMod.getTypeId());
        module.put("id", formMod.getId());
        module.put("name", formMod.getName());
        flowDto.setModule(module);
        // 流程模型
        flowDto.setProcessModel(mod);
        // 节点之间的关联关系
        flowDto.setModSequenceElements(sequenceElements);
        // 节点集合
        flowDto.setModElements(modElement);

        return flowDto;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> deleteFlow(Long flowId) throws Exception {
        int exeNum = 1;
        int status = 0;
        // 删除流程
        OaFlow flow = oaFlowDao.selectById(flowId);
        flow.setStatus(status);
        exeNum = oaFlowDao.updateById(flow);

        QueryWrapper<OaFlowMod> modQuery = new QueryWrapper<>();
        List<OaFlowMod> modList = oaFlowModDao.selectList(modQuery.eq("flow_id", flowId));
        for (OaFlowMod mod : modList) {
            // 删除流程模型
            if (exeNum > 0) {
                mod.setStatus(status);
                exeNum = oaFlowModDao.updateById(mod);
            }
            Map<String, Object> columnMap = new HashMap<String, Object>();
            columnMap.put("mod_id", mod.getId());
            List<OaFlowModDetail> details = oaFlowModDetailDao.selectByMap(columnMap);
            // 删除详情
            for (OaFlowModDetail detail : details) {
                if (exeNum > 0) {
                    detail.setStatus(status);
                    exeNum = oaFlowModDetailDao.updateById(detail);

                }
            }
            // 删除模型元素
            List<OaFlowModElement> elements = oaFlowModElementDao.selectByMap(columnMap);
            for (OaFlowModElement element : elements) {
                if (exeNum > 0) {
                    element.setStatus(status);
                    exeNum = oaFlowModElementDao.updateById(element);
                }
            }
            QueryWrapper<OaFlowForm> oaFlowForm = new QueryWrapper<>();
            List<OaFlowForm> flowFormList = oaFlowFormDao.selectList(oaFlowForm.eq("flow_id", flowId));
            for (OaFlowForm flowForm : flowFormList) {
                // 删除流程模型
                if (exeNum > 0) {
                    flowForm.setStatus(status);
                    exeNum =  oaFlowFormDao.updateById(flowForm);
                }
            }
        }

        if (exeNum <= 0) {
            throw new Exception();
        }
        return exeNum > 0 ? Result.ok() : Result.error();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> addOrEditFlow(@Valid OaFlowDTO oaFlowDTO) throws Exception {
        // 初始化参数
        int exeNum = 0;
        OaFlow oaFlow = new OaFlow();
        BeanUtils.copyProperties(oaFlowDTO, oaFlow);
        String flowUUid = UUIDUtils.getUUID();
        oaFlow.setUuid(flowUUid);
        // 流程模型元素
        List<OaFlowModElement> modElements = oaFlowDTO.getModElements();
        // 流程模型明细
        List<Map<String, Object>> modSequenceElements = oaFlowDTO.getModSequenceElements();
        //表单模型信息
        Map<String, Object> module = oaFlowDTO.getModule();

        // 新增流程名字不能重复
        OaFlow flow = oaFlowDao.selectOne(new QueryWrapper<OaFlow>().eq("flow_name", oaFlow.getFlowName()).eq("status", 1));
        if (null != flow && null == oaFlow.getId()) {
            return Result.error(20000, "该流程已存在，请重试！");
        }
        // 判断前端操作是不是对流程进行编辑
        flow = oaFlowDao.selectOne(new QueryWrapper<OaFlow>().eq("id", oaFlow.getId()));
        // 初始化流程模型版本号为1
        int flowModVersion = 1;
        if (null != flow) {
            // 流程已存在，流程版本增加1
            flowModVersion = flow.getFlowVersion() + 1;
            oaFlow.setFlowVersion(flowModVersion);
            exeNum = oaFlowDao.updateById(oaFlow);
        } else {
            oaFlow.setFlowVersion(flowModVersion);
            // 保存流程
            exeNum = oaFlowDao.insert(oaFlow);
        }
        // 赋值流程ID
        Long flowId = oaFlow.getId();
        if (null == module.get("id") || module.get("id").toString().isEmpty()) {
            throw new Exception("未绑定表单！");
        }
        // 流程模型，对前台传参进行非空判断
        OaFlowMod oaFlowMod = oaFlowDTO.getProcessModel() == null ? new OaFlowMod() : oaFlowDTO.getProcessModel();
        String modUUid = UUIDUtils.getUUID();
        oaFlowMod.setUuid(modUUid);
        // 和流程模型版本号保持一致
        oaFlowMod.setModVersion(flowModVersion);
        //重置ID
        oaFlowMod.setId(null);
        oaFlowMod.setFlowId(flowId);
        oaFlowMod.setFlowUuid(flowUUid);
        oaFlowMod.setFlowName(oaFlowDTO.getFlowName());
        // 保存流程模型
        if (exeNum > 0)
            exeNum = oaFlowModDao.insert(oaFlowMod);
        // 赋值流程模型ID和UUID
        Long flowModId = oaFlowMod.getId();
        // 表单模型ID
        Long formModId = Long.parseLong(module.get("id").toString());
        //查询表单流程组合状态为1的集合
        List<OaFlowForm> flowForms = oaFlowFormDao.selectList(new QueryWrapper<OaFlowForm>().eq("flow_id", flowId).eq("status", 1));
        if(null!=flowForms&&flowForms.size()>0) {
            for(OaFlowForm form:flowForms) {
                //流程更换表单将流程表单关联状态改成2，方便历史记录查询
                form.setStatus(2);
                //保证一个流程绑定的表单对应关系只有一个状态为1
                if(exeNum>0&&!formModId.equals(form.getFormModId()))
                    exeNum = oaFlowFormDao.updateById(form);
            }
        }
        OaFlowForm flowForm = new OaFlowForm();
        flowForm.setFlowId(flowId);
        flowForm.setFormModId(formModId);
        flowForm.setStatus(1);
        flowForm.setFlowModId(flowModId);
        if (exeNum > 0)
            exeNum = oaFlowFormDao.insert(flowForm);
        // 保存流程模型元素
        for (OaFlowModElement modElement : modElements) {
            if (exeNum > 0) {
                Map<String, Object> modElementConfig = modElement.getModElementConfig();
                OaFlowModConfig modConfig = new OaFlowModConfig();
                modConfig.setModConfig(JSON.toJSONString(modElementConfig));
                modElement.setId(null);
                modElement.setModId(flowModId);
                modElement.setModUuid(modUUid);
                exeNum = oaFlowModElementDao.insert(modElement);
                if (null != modElementConfig && !modElementConfig.isEmpty()) {
                    modConfig.setModElementId(modElement.getId());
                    if (exeNum > 0)
                        exeNum = oaFlowModConfigDao.insert(modConfig);
                }
            }
        }
        // 将线的关系转换成点的关系
        Set<OaFlowModDetail> details = new HashSet<OaFlowModDetail>();
        for (Map<String, Object> sequence : modSequenceElements) {
            OaFlowModDetail detail = new OaFlowModDetail();
            detail.setModId(flowModId);
            detail.setModUuid(modUUid);
            // 开始节点
            String fromCode = sequence.get("fromNodeCode").toString();
            // 指向节点
            String toCode = sequence.get("toNodeCode").toString();
            // 开始节点
            if ("startElement".equals(fromCode)) {
                detail.setNodeCode(fromCode);
                detail.setNextNodeCode(toCode);
                if (exeNum > 0)
                    details.add(detail);
                continue;
            }
            // 结束节点
            if ("endElement".equals(toCode)) {
                detail.setNodeCode(toCode);
                detail.setLastNodeCode(fromCode);
                if (exeNum > 0)
                    details.add(detail);
                for (Map<String, Object> oaSequence : modSequenceElements) {
                    if (fromCode.equals(oaSequence.get("toNodeCode"))) {
                        detail.setNodeCode(fromCode);
                        detail.setNextNodeCode(toCode);
                        detail.setLastNodeCode(oaSequence.get("fromNodeCode").toString());
                        if (exeNum > 0)
                            details.add(detail);
                    }
                    continue;
                }
                continue;
            }
            detail.setNodeCode(fromCode);
            detail.setNextNodeCode(toCode);
            for (Map<String, Object> oaSequence : modSequenceElements) {
                if (fromCode.equals(oaSequence.get("toNodeCode"))) {
                    detail.setLastNodeCode(oaSequence.get("fromNodeCode").toString());
                    if (exeNum > 0)
                        details.add(detail);
                }
                continue;
            }
        }
        for (OaFlowModDetail dd : details) {
            if (exeNum > 0)
                exeNum = oaFlowModDetailDao.insert(dd);
        }

        // 操作存在异常抛出，进行事务回滚
        if (exeNum <= 0) {
            throw new Exception("系统异常！");
        }
        Map<String, Object> flowMap = new HashMap<String, Object>();
        flowMap.put("id", flowId);
        return exeNum > 0 ? Result.ok(flowMap) : Result.error();
    }

    @Override
    public OaFlow selectFlowByForm(Long formId) {
        return oaFlowDao.selectFlowByForm(formId);
    }

    @Override
    public List<OaFlow> list(Wrapper<OaFlow> queryWrapper) {
        OaFlow OaFlow = queryWrapper.getEntity();
        return oaFlowDao.list(OaFlow);
    }
}