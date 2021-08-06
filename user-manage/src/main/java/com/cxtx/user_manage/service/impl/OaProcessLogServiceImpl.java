package com.cxtx.user_manage.service.impl;

import com.cxtx.common.unit.ServiceUtil;
import com.cxtx.user_manage.domain.User;
import com.cxtx.user_manage.mapper.UserMapper;
import com.cxtx.user_manage.service.UserService;
import com.cxtx.user_manage.unit.GuavaUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaProcessLog;
import com.cxtx.user_manage.mapper.OaProcessLogMapper;
import com.cxtx.user_manage.service.OaProcessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class OaProcessLogServiceImpl implements OaProcessLogService {


    @Autowired
    private OaProcessLogMapper oaProcessLogMapper;

    @Autowired
    private UserMapper userMapper;


    @Override
    public int deleteByPrimaryKey(Long id) {
        return oaProcessLogMapper.deleteByPrimaryKey(id);
    }


    @Override
    public int insertSelective(OaProcessLog record) {
        return oaProcessLogMapper.insertSelective(record);
    }


    @Override
    public int updateByPrimaryKeySelective(OaProcessLog record) {
        return oaProcessLogMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public OaProcessLog selectByPrimaryKey(Long id) {
        return oaProcessLogMapper.selectByPrimaryKey(id);
    }


    @Override
    public List<OaProcessLog> selectAll(Map params) {
        return oaProcessLogMapper.selectAll(params);
    }

    @Override
    public PageInfo<OaProcessLog> queryByPage(Map params) {
        ServiceUtil.checkPageParams(params);
        PageHelper.startPage(params);
        List<OaProcessLog> labels = oaProcessLogMapper.selectAll(params);
        return new PageInfo<OaProcessLog>(labels);
    }

    @Override
    public List<OaProcessLog> getLogByProcessId(Long processId) {
        List<OaProcessLog> logs = oaProcessLogMapper.getLogByProcessId(processId);
        for(OaProcessLog log:logs) {
            String curNodeName = "";
            String nextNodeName = "";
            List<String> curUserName = new ArrayList<String>();
            List<String> nextUserName = new ArrayList<String>();
            String cur_assignee = log.getCurAssignee();
            String next_assignee = log.getNextAssignees();
            System.out.println("当前审批人："+cur_assignee);
            System.out.println("下一节点审批人："+next_assignee);
            if(null != cur_assignee&&!(cur_assignee.isEmpty())) {
                List<User> curAssignee = userMapper.getUserInfoByIdStrings(cur_assignee);
                for(User u:curAssignee) {
                    curUserName.add(u.getLoginId());
                }
                log.setCurAssigneeName(GuavaUtil.list2string(curUserName, ","));
            }
            if(null != next_assignee&&!(next_assignee.isEmpty())) {
                List<User> nextAssignee = userMapper.getUserInfoByIdStrings(next_assignee);
                for(User u:nextAssignee) {
                    nextUserName.add(u.getLoginId());
                }
                log.setNextAssigneeName(GuavaUtil.list2string(nextUserName, ","));
            }
        }
        return logs;
    }

    @Override
    public int deleteByProcessId(Long processId) {
        return oaProcessLogMapper.deleteByProcessId(processId);
    }

}