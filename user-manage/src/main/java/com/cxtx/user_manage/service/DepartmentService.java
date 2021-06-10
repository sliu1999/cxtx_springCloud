//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cxtx.user_manage.service;

import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.Department;

import java.util.List;
import java.util.Map;

public interface DepartmentService {
    int deleteDepartment(String var1);

    int saveDepartment(Department var1);

    Department selectDepartmentById(String var1);

    List<Map> selectDepartmentTree();

    List<Map> selectDepartmentTreeTwo();

    List<Map> treeExceptMe(String departId);

    List<Map> treeExceptMeList(String departId);

    PageInfo<Department> selectDepartmentListByPage(Map var1);
}
