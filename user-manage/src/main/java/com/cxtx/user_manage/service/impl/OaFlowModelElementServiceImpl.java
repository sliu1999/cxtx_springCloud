package com.cxtx.user_manage.service.impl;

import com.cxtx.common.unit.ServiceUtil;
import com.cxtx.user_manage.domain.OaFlowModelDetail;
import com.cxtx.user_manage.mapper.OaFlowModelElementConfigMapper;
import com.cxtx.user_manage.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaFlowModelElement;
import com.cxtx.user_manage.mapper.OaFlowModelElementMapper;
import com.cxtx.user_manage.service.OaFlowModelElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    public int updateByPrimaryKey(OaFlowModelElement record) {
        return oaFlowModelElementMapper.updateByPrimaryKey(record);
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
            QueryWrapper<OaFlowModConfig> queryWrapper = new QueryWrapper<>();
            QueryWrapper<OaFlowModElement> queryWrapper1 = new QueryWrapper<>();
            OaFlowModelElement element = oaFlowModElementDao.selectOne(queryWrapper1.eq("code", detail.getLastNodeCode())
                    .eq("mod_id", detail.getModId()));
            OaFlowModConfig config = oaFlowModConfigDao.selectOne(queryWrapper.eq("mod_element_id", element.getId()));
            element.setParam(config.getModConfig());
            elements.add(element);
        }
        return elements;
    }
    @Override
    public List<OaFlowModelElement> getNextNodes(String curNode,Long modId) {
        List<OaFlowModElement> elements = new ArrayList<OaFlowModElement>();
        List<OaFlowModDetail> details = oaFlowModElementDao.getLastNextNodes(modId, curNode);
        for(OaFlowModDetail detail:details) {
            QueryWrapper<OaFlowModConfig> queryWrapper = new QueryWrapper<>();
            QueryWrapper<OaFlowModElement> queryWrapper1 = new QueryWrapper<>();
            OaFlowModElement element = oaFlowModElementDao.selectOne(queryWrapper1.eq("code", detail.getNextNodeCode())
                    .eq("mod_id", detail.getModId()));
            OaFlowModConfig config = oaFlowModConfigDao.selectOne(queryWrapper.eq("mod_element_id", element.getId()));
            //节点的配置信息
            element.setParam(config.getModConfig());
            elements.add(element);
        }
        return elements;
    }
    @Override
    public Map<String, Object> getNowNodeConfig(String curNode, Long modId) {
        return oaFlowModElementDao.getNowNodeConfig(modId, curNode);
    }
    @Override
    public List<Map<String, Object>> getNodeByProcessId(Long processId,Long tenantId) {
        List<Map<String,Object>> nodes = oaFlowModElementDao.getNodeByProcessId(processId);
        for(Map<String,Object> node:nodes) {
            String idStrings = (String) node.get("cur_assignee");
            node.put("assigneeName",sysUserDao.getUserNameByIdStrings(idStrings, tenantId));
        }
        return nodes;
    }
}