//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cxtx.user_manage.mapper;

import com.cxtx.user_manage.domain.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author DELL
 */
public interface MenuMapper {
    int deleteMenuByIds(String var1);

    int deleteRole2MenusByMenuIds(String var1);

    String selectSubMenusIds(String var1);

    int insertMenu(Menu var1);

    int updateMenu(Menu var1);

    List<Map> selectMenusTree();

    List<Menu> selectMenuModulesByRoleId(@Param("id") String var1, @Param("roleId") String var2);

    List<Menu> selectModuleLimitsByRoleId(@Param("roleId") String var1, @Param("menuId") String var2);

    List<Menu> selectStairMenuByUserId(@Param("userId") String userId);

    List<Menu> selectChildrenMenuByParentId(@Param("parentMenuId") String parentMenuId);
}
