//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cxtx.user_manage.service.impl;

import com.cxtx.common.unit.ServiceUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.Role;
import com.cxtx.user_manage.mapper.RoleMapper;
import com.cxtx.user_manage.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(
        rollbackFor = {Exception.class}
)
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    public RoleServiceImpl() {
    }

    @Override
    public int saveRole(Role role) {
        byte result;
        if (role.getId() != null) {
            if (role.getName() != null || role.getRemark() != null) {
                this.roleMapper.updateRole(role);
            }

            this.roleMapper.deleteRole2MenusByRoleId(role.getId());
            if (role.getMenuIds() != null && role.getMenuIds().size() > 0) {
                this.roleMapper.insertRole2Menus(role);
            }

            result = 1;
        } else {
            if (role.getName() != null || role.getRemark() != null) {
                this.roleMapper.insertRole(role);
            }

            this.roleMapper.deleteRole2MenusByRoleId(role.getId());
            if (role.getMenuIds() != null && role.getMenuIds().size() > 0) {
                this.roleMapper.insertRole2Menus(role);
            }

            result = 1;
        }

        return result;
    }

    @Override
    public int deleteRoleById(String id) {
        this.roleMapper.deleteRole2MenusByRoleId(id);
        return this.roleMapper.deleteRoleById(id);
    }

    @Override
    public Role selectRoleById(String id) {
        return this.roleMapper.selectRoleById(id);
    }

    @Override
    public List<String> selectRoleListByUserId(String userId) {
        return this.roleMapper.selectRoleListByUserId(userId);
    }

    @Override
    public PageInfo<Role> selectRolesByPage(Map params) {
        ServiceUtil.checkPageParams(params);
        PageHelper.startPage(params);
        List<Role> roles = this.roleMapper.selectRoleList(params);
        return new PageInfo(roles);
    }

    @Override
    public List<Role> selectAllRole() {
        return roleMapper.selectAllRole();
    }
}
