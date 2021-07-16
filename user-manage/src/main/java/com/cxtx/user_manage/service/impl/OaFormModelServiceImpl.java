package com.cxtx.user_manage.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cxtx.common.unit.ServiceUtil;
import com.cxtx.user_manage.domain.*;
import com.cxtx.user_manage.mapper.OaFlowMapper;
import com.cxtx.user_manage.mapper.OaFlowModelElementMapper;
import com.cxtx.user_manage.mapper.OaFlowModelMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.mapper.OaFormModelMapper;
import com.cxtx.user_manage.service.OaFormModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    public int updateByPrimaryKeySelective(OaFormModel record) {
        return oaFormModelMapper.updateByPrimaryKeySelective(record);
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
}