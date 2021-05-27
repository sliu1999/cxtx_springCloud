

package com.cxtx.user_manage.service;

import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.Staff;

import java.util.Map;

public interface StaffService {
    int saveStaff(Staff var1) throws Exception;

    Staff selectStaffById(String var1);

    Staff selectStaffByLoginId(String var1);

    int deleteStaffById(String var1) throws Exception;

    PageInfo<Staff> selectStaffListByPage(Map var1) throws Exception;

    Boolean duplicateCheckStaffNo(String var1, String var2);
}
