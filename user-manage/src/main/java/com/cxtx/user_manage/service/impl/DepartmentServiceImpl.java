
package com.cxtx.user_manage.service.impl;

import com.cxtx.common.unit.ServiceUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.Department;
import com.cxtx.user_manage.mapper.DepartmentMapper;
import com.cxtx.user_manage.service.DepartmentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(
        rollbackFor = {Exception.class}
)
public class DepartmentServiceImpl implements DepartmentService {
    private static Logger logger = LoggerFactory.getLogger(DepartmentServiceImpl.class);
    @Autowired
    private DepartmentMapper departmentMapper;


    @Override
    public int deleteDepartment(String id) {
        String ids = this.departmentMapper.selectSubDepartmentsIds(id);
        this.departmentMapper.deleteStaff2DepartmentsByDepartmentIds(ids);
        return this.departmentMapper.deleteDepartment(ids);
    }

    @Override
    public int saveDepartment(Department department) {
        return department.getId() == null ? this.departmentMapper.insertDepartment(department) : this.departmentMapper.updateDepartment(department);
    }

    @Override
    public Department selectDepartmentById(String id) {
        return this.departmentMapper.selectDepartmentById(id);
    }

    @Override
    public List<Map> selectDepartmentTree() {
        return this.departmentMapper.selectDepartmentTree();
    }

    @Override
    public List<Map> selectDepartmentTreeTwo() {
        List<Map> allList = this.departmentMapper.selectAllDepartment();
        List<Map> parentList = new ArrayList<>();
        for(Map all : allList){
            if(all.get("parentId") == null){
                Map parent = new HashMap(4);
                parent.put("id",all.get("id"));
                parent.put("label",all.get("label"));
                parent.put("parentId",null);
                parentList.add(parent);
            }
        }
        for(Map parent : parentList){
            buildChildTree(parent,allList);
        }
        return parentList;
    }


    @Override
    public List<Map> treeExceptMe(String departId) {
        List<Map> allListExceptMe = departmentMapper.selectAllExceptMe(departId);
        List<Map> parentList = new ArrayList<>();
        for(Map all : allListExceptMe){
            if(all.get("parentId") == null){
                Map parent = new HashMap(4);
                parent.put("id",all.get("id"));
                parent.put("label",all.get("label"));
                parent.put("parentId",null);
                parentList.add(parent);
            }
        }
        for(Map parent : parentList){
            buildChildTree(parent,allListExceptMe);
        }
        return parentList;
    }

    @Override
    public List<Map> treeExceptMeList(String departId) {
        return departmentMapper.selectAllExceptMe(departId);
    }

    private Map buildChildTree(Map pNode,List<Map> allList){
        List<Map> childMenus =new ArrayList<>();
        for(Map menuNode : allList) {
            if(menuNode.get("parentId") != null && !"".equals(menuNode.get("parentId"))){
                if(menuNode.get("parentId").equals(pNode.get("id"))) {
                    childMenus.add(buildChildTree(menuNode,allList));
                }
            }
        }
        if(childMenus!=null && childMenus.size()>0){
            pNode.put("children",childMenus);
        }
        return pNode;
    }

    @Override
    public PageInfo<Department> selectDepartmentListByPage(Map params) {
        ServiceUtil.checkPageParams(params);
        PageHelper.startPage(params);
        List<Department> departments = this.departmentMapper.selectDepartmentList(params);
        return new PageInfo(departments);
    }
}
