//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cxtx.user_manage.mapper;

import com.cxtx.user_manage.domain.Department;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DepartmentMapper {
    int deleteDepartment(String var1);

    int deleteStaff2DepartmentsByDepartmentIds(String var1);

    String selectSubDepartmentsIds(String var1);

    int insertDepartment(Department var1);

    int updateDepartment(Department var1);

    Department selectDepartmentById(String var1);

    List<Map> selectDepartmentTree();

    List<Map> selectAllDepartment();

    List<Map> selectAllExceptMe(@Param("departId") String departId);

    List<Department> selectDepartmentList(Map var1);

}
