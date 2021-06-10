

package com.cxtx.user_manage.service;

import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.User;
import com.cxtx.user_manage.web.rest.vo.ChangeCurrentUserPasswordDTO;
import com.cxtx.user_manage.web.rest.vo.ChangeUserPasswordDTO;

import java.util.List;
import java.util.Map;

public interface UserService {
    int deleteUserById(String var1);

    int deleteUserByLoginId(String var1);

    int saveUser(User var1) throws Exception;

    int updateUserDefaultRole(String var1, String var2) throws Exception;

    User selectUserById(String var1);

    User selectUserByLoginId(String var1);

    PageInfo<User> selectUsersByPage(Map var1);

    List<User> selectUserList();

    Boolean checkUser2Role(String var1, String var2);

    Boolean duplicateCheckLoginId(String var1, String var2);

    Boolean checkLimit2ChangePassword(String var1);

    int changeUserPassword(ChangeUserPasswordDTO var1) throws Exception;

    int changeCurrentUserPassword(ChangeCurrentUserPasswordDTO var1) throws Exception;
}
