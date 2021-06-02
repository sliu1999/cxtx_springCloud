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

import java.util.ArrayList;
import java.util.HashMap;
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
    public List<Map> selectMenusTreeTwo() {
        List<Map> allList = this.menuMapper.selectAllMenu();
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
    public List<Map> queryRoleMenusTree(String roleId) {
        List<Map> stairList = menuMapper.selectRoleStairMenu(roleId);
        List<Map> subList = menuMapper.selectRoleSubMenu(roleId);
        List<Map> parentList = new ArrayList<>();
        for(Map stair : stairList){
            if(stair.get("parentId") == null){
                Map parent = new HashMap(4);
                parent.put("id",stair.get("id"));
                parent.put("label",stair.get("label"));
                parentList.add(parent);
            }
        }
        for(Map parent : parentList){
            buildChildTree(parent,subList);
        }
        return parentList;
    }


    private Map buildChildTree(Map pNode,List<Map> allList){
        List<Map> childMenus =new  ArrayList<>();
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
    public List<Menu> queryAllStairMenu() {
        return menuMapper.selectAllStairMenu();
    }

    @Override
    public Menu queryMenuInfo(String id) {
        return menuMapper.queryMenuInfo(id);
    }

    @Override
    public List<Menu> selectChildrenMenuByParentId(String parentMenuId) {
        return menuMapper.selectChildrenMenuByParentId(parentMenuId);
    }
}
