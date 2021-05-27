//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cxtx.user_manage.mapper;

import com.cxtx.user_manage.domain.User;
import com.cxtx.user_manage.web.rest.vo.ChangeCurrentUserPasswordDTO;
import com.cxtx.user_manage.web.rest.vo.ChangeUserPasswordDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    int deleteUserById(String var1);

    int deleteUserByLoginId(String var1);

    int deleteUser2RoleById(String var1);

    int deleteUser2RoleByLoginId(String var1);

    int insertUser(User var1);

    int insertUser2Roles(User var1);

    int updateUser2RolesDefault(@Param("loginId") String var1, @Param("roleId") String var2);

    int updateUser(User var1);

    User selectUserById(String var1);

    User selectUserByLoginId(String var1);

    List<User> selectUserList(Map var1);

    Integer selectCountByLoginIdAndRoleId(@Param("loginId") String var1, @Param("roleId") String var2);

    Integer selectCountByLoginId(@Param("loginId") String var1, @Param("id") String var2);

    Integer selectPasswordLimitIndexByLoginId(String var1);

    int updateUserPasswordById(ChangeUserPasswordDTO var1);

    int updateUserPasswordByLoginId(ChangeCurrentUserPasswordDTO var1);

    Integer selectCountByLoginIdAndPassword(@Param("loginId") String var1, @Param("password") String var2);
}
