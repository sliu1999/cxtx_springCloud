package com.cxtx.user_manage.service.impl;

import com.cxtx.common.unit.ServiceUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaProcessRun;
import com.cxtx.user_manage.mapper.OaProcessRunMapper;
import com.cxtx.user_manage.service.OaProcessRunService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@Transactional(rollbackFor = Exception.class)
public class OaProcessRunServiceImpl implements OaProcessRunService {


    @Autowired
    private OaProcessRunMapper oaProcessRunMapper;


    @Override
    public int deleteByPrimaryKey(Long id) {
        return oaProcessRunMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(OaProcessRun record) {
        return oaProcessRunMapper.insert(record);
    }

    @Override
    public int insertSelective(OaProcessRun record) {
        return oaProcessRunMapper.insertSelective(record);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Override
    public OaProcessRun selectByPrimaryKey(Long id) {
        return oaProcessRunMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(OaProcessRun record) {
        return oaProcessRunMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(OaProcessRun record) {
        return oaProcessRunMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<OaProcessRun> selectAll(Map params) {
        return oaProcessRunMapper.selectAll(params);
    }

    @Override
    public PageInfo<OaProcessRun> queryByPage(Map params) {
        ServiceUtil.checkPageParams(params);
        PageHelper.startPage(params);
        List<OaProcessRun> labels = oaProcessRunMapper.selectAll(params);
        return new PageInfo<OaProcessRun>(labels);
    }


    @Autowired
    OaProcessRunDao oaProcessRunDao;
    @Autowired
    OaProcessLogDao oaProcessLogDao;
    @Autowired
    SysUserDao sysUserDao;
    @Override
    public List<OaProcessRun> getMyProcess(Long userId) {
        QueryWrapper<OaProcessRun> wrapper = new QueryWrapper<>();
        return oaProcessRunDao.selectList(wrapper.eq("user_id", userId));
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Page getRunProcess(@Param("map")Map<String,Object> map, int pageNum, int pageSize) {
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
        //待办流程总数
        List<Map<String,Object>> run = oaProcessRunDao.getRunProcess(map);
        //分页查询的数据
        List<Map<String,Object>> runPage = oaProcessRunDao.getRunProcessByLimit(map, (pageNum-1)*pageSize, pageSize);
        Page page = new Page<>(pageNum, pageSize, run.size(), true);
        for(Map<String,Object> mm:runPage) {
            Long processId = Long.parseLong(mm.get("id").toString());
            QueryWrapper<OaProcessLog> wrapper = new QueryWrapper<>();
            OaProcessLog log = oaProcessLogDao.selectList(wrapper.eq("process_id", processId).orderByDesc("create_date")).get(0);
            mm.put("lastHandler",sysUserDao.selectById(log.getCurAssignee()).getTrueName());
            result.add(mm);
        }
        page.setCurrent(pageNum);
        page.setRecords(result);
        return page;
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public IPage<?> getRunProcess(Page page, Map<String,Object> data) {
        List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
        IPage runPage = oaProcessRunDao.getRunProcess(page, data);
        List<Map<String,Object>> list = runPage.getRecords();
        for(Map<String,Object> mm:list) {
            Long processId = Long.parseLong(mm.get("id").toString());
            QueryWrapper<OaProcessLog> wrapper = new QueryWrapper<>();
            OaProcessLog log = oaProcessLogDao.selectList(wrapper.eq("process_id", processId).orderByDesc("create_date")).get(0);
            if(sysUserDao.selectById(log.getCurAssignee())!=null) {
                mm.put("lastHandler",sysUserDao.selectById(log.getCurAssignee()).getTrueName());
            }
            result.add(mm);
        }
        runPage.setRecords(result);
        return runPage;
    }
}