package com.cxtx.user_manage.service.impl;

import com.cxtx.common.unit.ServiceUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaProcessHis;
import com.cxtx.user_manage.mapper.OaProcessHisMapper;
import com.cxtx.user_manage.service.OaProcessHisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Transactional(rollbackFor = Exception.class)
public class OaProcessHisServiceImpl implements OaProcessHisService {


    @Autowired
    private OaProcessHisMapper oaProcessHisMapper;


    @Override
    public int deleteByPrimaryKey(Long id) {
        return oaProcessHisMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(OaProcessHis record) {
        return oaProcessHisMapper.insert(record);
    }

    @Override
    public int insertSelective(OaProcessHis record) {
        return oaProcessHisMapper.insertSelective(record);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public OaProcessHis selectByPrimaryKey(Long id) {
        return oaProcessHisMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(OaProcessHis record) {
        return oaProcessHisMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(OaProcessHis record) {
        return oaProcessHisMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<OaProcessHis> selectAll(Map params) {
        return oaProcessHisMapper.selectAll(params);
    }

    @Override
    public PageInfo<OaProcessHis> queryByPage(Map params) {
        ServiceUtil.checkPageParams(params);
        PageHelper.startPage(params);
        List<OaProcessHis> labels = oaProcessHisMapper.selectAll(params);
        return new PageInfo<OaProcessHis>(labels);
    }

    @Autowired
    OaProcessHisDao oaProcessHisDao;
    @Autowired
    OaProcessLogDao oaProcessLogDao;
    @Autowired
    SysUserDao sysUserDao;
    @Autowired
    OaFlowService oaFlowService;
    @Autowired
    OaFormModDao oaFormModDao;
    @Autowired
    OaFlowDao oaFlowDao;
    @Override
    public List<OaProcessHis> getMyHisProcess(Long userId) {
        QueryWrapper<OaProcessHis> wrapper = new QueryWrapper<>();
        return oaProcessHisDao.selectList(wrapper.eq("user_id", userId));
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Page getHisProcess(Map<String,Object> map,int pageNum,int pageSize) {
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
        //总数
        //List<Map<String,Object>> his = oaProcessHisDao.getHisProcess(map);
        Page page = new Page<>(pageNum, pageSize);
        //分页数据
        List<Map<String,Object>> hisPage = oaProcessHisDao.getHisProcessByLimit(page,map);
        Iterator<Map<String, Object>> iterator = hisPage.iterator();
        while (iterator.hasNext()) {
            Map<String, Object> temp = iterator.next();
            Long flowModId = Long.parseLong(temp.get("mod_id").toString());
            List<OaFlow> oaFlowList = oaFlowDao
                    .selectList(new QueryWrapper<OaFlow>().eq("flow_name",temp.get("processName")));
            if(oaFlowList.size()>0) {
                OaFormMod mod = oaFormModDao.getFormModByFlowModId(flowModId);
                //流程和实例绑定的表单被真删或者假删，流程和实例均无法查看
                if(mod!=null) {
                    if(mod.getStatus()==0) {
                        iterator.remove();
                    }
                }
            }

        }
        //Page page = new Page<>(pageNum, pageSize);
        for(Map<String,Object> mm:hisPage) {
            Long processId = Long.parseLong(mm.get("id").toString());
            QueryWrapper<OaProcessLog> wrapper = new QueryWrapper<>();
            OaProcessLog log = oaProcessLogDao.selectList(wrapper.eq("process_id", processId).orderByDesc("create_date")).get(0);
            if (null != sysUserDao.selectById(log.getCurAssignee())) {
                mm.put("lastHandler",sysUserDao.selectById(log.getCurAssignee()).getTrueName());
            }
            String currentHandler = null;
            if(null != log.getNextAssignees()&&!(log.getNextAssignees().isEmpty())) {
                currentHandler = GuavaUtil.list2string(sysUserDao.getUserNameByIdStrings(log.getNextAssignees(), log.getTenantId()),"，");
            }
            mm.put("currentHandler",currentHandler);
            mm.put("isDelete", "0");
            result.add(mm);
        }

        page.setRecords(hisPage);
        page.setCurrent(pageNum);
        return page;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public IPage getHisProcessByPage(Page page, Map<String, Object> map) {
        return oaProcessHisDao.getHisProcessByPage(page, map);
    }
    @Override
    public List<Map<String,Object>>newgetHisProcess(Map<String, Object> map) {
        //总数
        return oaProcessHisDao.newgetHisProcess(map);
    }
    @SuppressWarnings({ "rawtypes", "unused", "unchecked" })
    @Override
    public IPage getHisProcessByAuth(Long curUserId, int pageSize, int pageNum, Map<String, Object> map) {
        // TODO Auto-generated method stub
        Page page = new Page(pageNum,pageSize);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> userMap = sysUserDao.queryUserListById(curUserId.toString());
        List<Map<String, Object>> mainAuthList = oaProcessHisDao.getHistoryMainAuthList(userMap);
        System.err.println("+++++++++++++++++++222222"+mainAuthList );
        int userFlag = 0;
        Set<String> userSet = new HashSet();
        String sqlStr = "";
        if(mainAuthList.size()>0) {
            for (Map<String, Object> map2 : mainAuthList) {
                List<GroupRoleEntity> grList = oaProcessHisDao.getHistorySeeList(map2);
                if(grList != null && grList.size() != 0 && grList.get(0) != null) {
                    for (GroupRoleEntity groupRoleEntity : grList) {
                        List<String> dataList = oaProcessHisDao.getUserListBySeeList(groupRoleEntity);
                        if(groupRoleEntity.getUserId() != null && groupRoleEntity.getUserId().equals("all")) {
                            userFlag = 1;
                            break;
                        }
                        userSet.addAll(dataList);
                    }
                }
                String Str = "SELECT pro.id,pro.process_name AS processName,u.true_name AS creator,uu.true_name AS currentHandler,"
                        + "pro.create_date AS createDate,pro.`code`,pro.code_name AS codeName,his.create_date AS handleDate,"
                        + "pro.state,pro.mod_id,b.is_delete AS isDelete  "
                        + "FROM oa_process pro "
                        + "inner JOIN oa_process_his his ON pro.id = his.process_id "
                        + "inner JOIN sys_user u ON pro.create_by = u.id "
                        + "LEFT JOIN sys_user uu ON pro.assignee = uu.id "
                        + "INNER JOIN oa_flow_form off ON off.flow_mod_id = pro.mod_id "
                        + "inner join (select form_id,is_delete from oa_form_history_second_auth where main_id = "+map2.get("id")+" group by form_id) b"
                        + " on off.form_mod_id = b.form_id ";
                String userStr = null;
                if(userFlag == 0) {
                    if(userSet.size()!=0) {
                        for (String string : userSet) {
                            userStr += string+",";
                        }
                        userStr = " where his.user_id IN ("+userStr.substring(4,userStr.length()-1)+") and off.status !=0 group by id";
                    }else {
                        userStr = " where 1=0 group by id";
                    }
                }else {
                    userStr = " where off.status !=0  group by id";
                }
                sqlStr += Str+userStr+" union All ";
                userFlag = 0;
            }
            //当前用户自己发起的记录
            sqlStr=sqlStr+"SELECT pro.id,pro.process_name AS processName,u.true_name AS creator,uu.true_name AS currentHandler,"
                    + "pro.create_date AS createDate,pro.`code`,pro.code_name AS codeName,his.create_date AS handleDate,"
                    + "pro.state,pro.mod_id,'0' AS isDelete "
                    + "FROM oa_process pro "
                    + "inner JOIN oa_process_his his ON pro.id = his.process_id "
                    + "inner JOIN sys_user u ON pro.create_by = u.id "
                    + "LEFT JOIN sys_user uu ON pro.assignee = uu.id "
                    + "inner JOIN oa_flow_form off ON pro.mod_id = off.flow_mod_id "
                    + "where his.user_id ="+curUserId+" and off.status !=0";
            //map.put("sqlStr", sqlStr.substring(0, sqlStr.length()-10));
            map.put("sqlStr", sqlStr);
            IPage finalList = oaProcessHisDao.getDataListByUser(page, map);
            return finalList;

        }else {//目前登陆用户自己发起的记录
            map.put("userId", curUserId);
            IPage defaultList =oaProcessHisDao.getHisProcessByPage(page, map);
            return defaultList;

        }
    }
}