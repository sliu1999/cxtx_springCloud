

package com.cxtx.user_manage.service;

import com.cxtx.user_manage.domain.Menu;

import java.util.List;
import java.util.Map;

public interface MenuService {
    int deleteMenuById(String var1);

    int saveMenu(Menu var1);

    List<Map> selectMenusTree();

    List<Menu> selectMenuModulesByRoleId(String var1, String var2) throws Exception;

    List<Menu> selectModuleLimitsByRoleId(Map params) throws Exception;

    List<Menu> selectStairMenuByUserId(String userId);

    List<Menu> selectChildrenMenuByParentId(String parentMenuId);
}
