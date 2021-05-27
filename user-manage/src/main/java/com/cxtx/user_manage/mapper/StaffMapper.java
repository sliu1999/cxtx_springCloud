//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cxtx.user_manage.mapper;

import com.cxtx.user_manage.domain.Staff;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface StaffMapper {
    int deleteStaff2Departments(String var1);

    int insertStaff2Departments(Staff var1);

    int insertStaff(Staff var1);

    int updateStaff(Staff var1);

    Staff selectStaffById(String var1);

    Staff selectStaffByLoginId(String var1);

    int deleteStaffById(String var1);

    List<Staff> selectStaffList(Map var1);

    Integer selectCountByStaffNo(@Param("staffNo") String var1, @Param("id") String var2);
}
