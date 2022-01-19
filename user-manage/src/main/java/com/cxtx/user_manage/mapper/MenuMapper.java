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

    List<Map> selectAllMenu();

    List<Map> selectRoleStairMenu(@Param("roleId") String roleId);

    List<Map> selectRoleSubMenu(@Param("roleId") String roleId);

    List<Menu> selectAllStairMenu();

    Menu queryMenuInfo(@Param("id") String id);

    List<Menu> getAllByUserId(@Param("userId") String userId);
}
