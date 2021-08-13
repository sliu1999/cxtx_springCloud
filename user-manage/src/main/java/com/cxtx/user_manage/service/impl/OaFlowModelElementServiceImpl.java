package com.cxtx.user_manage.service.impl;

import com.cxtx.common.unit.ServiceUtil;
import com.cxtx.user_manage.domain.OaFlowModel;
import com.cxtx.user_manage.domain.OaFlowModelDetail;
import com.cxtx.user_manage.domain.OaFlowModelElementConfig;
import com.cxtx.user_manage.mapper.OaFlowModelElementConfigMapper;
import com.cxtx.user_manage.mapper.UserMapper;
import com.cxtx.user_manage.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaFlowModelElement;
import com.cxtx.user_manage.mapper.OaFlowModelElementMapper;
import com.cxtx.user_manage.service.OaFlowModelElementService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional(rollbackFor = Exception.class)
public class OaFlowModelElementServiceImpl implements OaFlowModelElementService {


    @Autowired
    private OaFlowModelElementMapper oaFlowModelElementMapper;

    @Autowired
    OaFlowModelElementConfigMapper oaFlowModelElementConfigMapper;

    @Autowired
    private UserService userService;

    @Autowired
    UserMapper userMapper;


    @Override
    public int deleteByPrimaryKey(Long id) {
        return oaFlowModelElementMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(OaFlowModelElement record) {
        return oaFlowModelElementMapper.insert(record);
    }

    @Override
    public int insertSelective(OaFlowModelElement record) {
        return oaFlowModelElementMapper.insertSelective(record);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public OaFlowModelElement selectByPrimaryKey(Long id) {
        return oaFlowModelElementMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(OaFlowModelElement record) {
        return oaFlowModelElementMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<OaFlowModelElement> selectAll(Map params) {
        return oaFlowModelElementMapper.selectAll(params);
    }

    @Override
    public PageInfo<OaFlowModelElement> queryByPage(Map params) {
        ServiceUtil.checkPageParams(params);
        PageHelper.startPage(params);
        List<OaFlowModelElement> labels = oaFlowModelElementMapper.selectAll(params);
        return new PageInfo<OaFlowModelElement>(labels);
    }

    @Override
    public List<OaFlowModelElement> getLastNodes(String curNode,Long modId) {
        List<OaFlowModelElement> elements = new ArrayList<OaFlowModelElement>();
        List<OaFlowModelDetail> details = oaFlowModelElementMapper.getLastNextNodes(modId, curNode);
        for(OaFlowModelDetail detail:details) {
            OaFlowModelElement element = oaFlowModelElementMapper.selectOneByCodeAndModelId(detail.getLastNodeCode(),detail.getModelId());
            OaFlowModelElementConfig config = oaFlowModelElementConfigMapper.selectByElementId(element.getId());
            element.setParam(config.getElementConfig());
            elements.add(element);
        }
        return elements;
    }
    @Override
    public List<OaFlowModelElement> getNextNodes(String curNode,Long modId) {
        List<OaFlowModelElement> elements = new ArrayList<OaFlowModelElement>();
        List<OaFlowModelDetail> details = oaFlowModelElementMapper.getLastNextNodes(modId, curNode);
        for(OaFlowModelDetail detail:details) {
            OaFlowModelElement element = oaFlowModelElementMapper.selectOneByCodeAndModelId(detail.getNextNodeCode(),detail.getModelId());
            OaFlowModelElementConfig config = oaFlowModelElementConfigMapper.selectByElementId(element.getId());
            //节点的配置信息
            element.setParam(config.getElementConfig());
            elements.add(element);
        }
        return elements;
    }
    @Override
    public Map<String, Object> getNowNodeConfig(String curNode, Long flowModelId) {
        return oaFlowModelElementMapper.getNowNodeConfig(flowModelId, curNode);
    }
    @Override
    public List<Map<String, Object>> getNodeByProcessId(Long processId) {
        List<Map<String,Object>> nodes = oaFlowModelElementMapper.getNodeByProcessId(processId);
        for(Map<String,Object> node:nodes) {
            String idStrings = (String) node.get("cur_assignee");
            node.put("assigneeName",userMapper.getUserNameByIdStrings(idStrings));
        }
        return nodes;
    }

    @Override
    public OaFlowModelElement selectOneByCodeAndModelId(String code, Long modelId) {
        return oaFlowModelElementMapper.selectOneByCodeAndModelId(code,modelId);
    }

    @Override
    public Map<String, Object> getElementConfigByFlowIdAndCurNode(HashMap param) {
        return oaFlowModelElementMapper.getElementConfigByFlowIdAndCurNode(param);
    }
}