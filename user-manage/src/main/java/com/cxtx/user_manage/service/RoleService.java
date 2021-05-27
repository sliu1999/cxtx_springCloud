

package com.cxtx.user_manage.service;

import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.Role;

import java.util.List;
import java.util.Map;

public interface RoleService {
    int saveRole(Role var1);

    int deleteRoleById(String var1);

    Role selectRoleById(String var1);

    List<String> selectRoleListByUserId(String userId);

    PageInfo<Role> selectRolesByPage(Map var1);

    List<Role> selectAllRole();
}
