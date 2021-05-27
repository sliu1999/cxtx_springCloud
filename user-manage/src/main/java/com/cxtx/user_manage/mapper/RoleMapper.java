//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cxtx.user_manage.mapper;

import com.cxtx.user_manage.domain.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper {
    int insertRole(Role var1);

    int updateRole(Role var1);

    int insertRole2Menus(Role var1);

    int deleteRoleById(String var1);

    int deleteRole2MenusByRoleId(String var1);

    Role selectRoleById(String var1);

    List<String> selectRoleListByUserId(String userId);

    List<Role> selectRoleList(Map var1);

    List<Role> selectAllRole();
}
