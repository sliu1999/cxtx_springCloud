

package com.cxtx.user_manage.service;

import com.cxtx.user_manage.domain.Menu;

import java.util.List;
import java.util.Map;

public interface MenuService {
    int deleteMenuById(String var1);

    int saveMenu(Menu var1);

    List<Map> selectMenusTreeTwo();

    List<Map> queryRoleMenusTree(String roleId);

    List<Menu> queryAllStairMenu();

    Menu queryMenuInfo(String id);


    /**
     * 获取所有管理端路由
     * @return
     */
    List<Menu> getAllByUserId(String userId);
}
