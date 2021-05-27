//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cxtx.user_manage.service.impl;

import com.cxtx.user_manage.domain.Menu;
import com.cxtx.user_manage.mapper.MenuMapper;
import com.cxtx.user_manage.service.MenuService;
import com.cxtx.user_manage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(
        rollbackFor = {Exception.class}
)
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private UserService userService;

    public MenuServiceImpl() {
    }

    @Override
    public int deleteMenuById(String id) {
        String ids = this.menuMapper.selectSubMenusIds(id);
        this.menuMapper.deleteRole2MenusByMenuIds(ids);
        return this.menuMapper.deleteMenuByIds(ids);
    }

    @Override
    public int saveMenu(Menu menu) {
        return menu.getId() == null ? this.menuMapper.insertMenu(menu) : this.menuMapper.updateMenu(menu);
    }

    @Override
    public List<Map> selectMenusTree() {
        return this.menuMapper.selectMenusTree();
    }

    @Override
    public List<Menu> selectMenuModulesByRoleId(String roleId,String loginId) throws Exception {
        if (this.userService.checkUser2Role(loginId, roleId)) {
            return this.menuMapper.selectMenuModulesByRoleId((String)null, roleId);
        } else {
            throw new Exception("当前登陆用户没有权限……");
        }
    }

    @Override
    public List<Menu> selectModuleLimitsByRoleId(Map params) throws Exception {
        if (this.userService.checkUser2Role((String) params.get("loginId"),(String)params.get("roleId"))) {
            return this.menuMapper.selectModuleLimitsByRoleId((String)params.get("roleId"),(String) params.get("menuId"));
        } else {
            throw new Exception("当前登陆用户没有权限……");
        }
    }

    @Override
    public List<Menu> selectStairMenuByUserId(String userId) {
        return menuMapper.selectStairMenuByUserId(userId);
    }

    @Override
    public List<Menu> selectChildrenMenuByParentId(String parentMenuId) {
        return menuMapper.selectChildrenMenuByParentId(parentMenuId);
    }
}
