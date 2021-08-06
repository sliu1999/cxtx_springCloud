//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cxtx.user_manage.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.Staff;
import com.cxtx.user_manage.domain.User;
import com.cxtx.user_manage.mapper.UserMapper;
import com.cxtx.user_manage.service.StaffService;
import com.cxtx.user_manage.service.UserService;
import com.cxtx.common.unit.ServiceUtil;
import com.cxtx.common.unit.Md5;
import com.cxtx.user_manage.web.rest.vo.ChangeCurrentUserPasswordDTO;
import com.cxtx.user_manage.web.rest.vo.ChangeUserPasswordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(
        rollbackFor = {Exception.class}
)
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StaffService staffService;

    @Override
    public int deleteUserById(String id) {
        this.userMapper.deleteUser2RoleById(id);
        return this.userMapper.deleteUserById(id);
    }

    @Override
    public int deleteUserByLoginId(String loginId) {
        this.userMapper.deleteUser2RoleByLoginId(loginId);
        return this.userMapper.deleteUserByLoginId(loginId);
    }

    @Override
    public int saveUser(User user) throws Exception {
        if (user.getLoginId() == null) {
            throw new Exception("账号不可为空……");
        } else if (!this.duplicateCheckLoginId(user.getLoginId(), user.getId())) {
            throw new Exception("账号重复，不可用……");
        } else if (user.getId() != null) {
            this.userMapper.deleteUser2RoleById(user.getId());
            if (user.getRoles() != null && user.getRoles().size() > 0) {
                this.userMapper.insertUser2Roles(user);
            }

            return this.userMapper.updateUser(user);
        } else {
            String md5PW;
            if (user.getPassword() != null && !"".equals(user.getPassword())) {
                md5PW = Md5.md5Encode(user.getPassword());
            } else {
                md5PW = Md5.md5Encode(user.getLoginId());
            }

            user.setPassword(md5PW);
            int result = this.userMapper.insertUser(user);
            if (user.getRoles() != null && user.getRoles().size() > 0) {
                this.userMapper.insertUser2Roles(user);
            }

            return result;
        }
    }

    @Override
    public int updateUserDefaultRole(String loginId, String roleId) throws Exception {
        if (this.checkUser2Role(loginId, roleId)) {
            return this.userMapper.updateUser2RolesDefault(loginId, roleId);
        } else {
            throw new Exception("当前登陆用户没有该角色的权限……");
        }
    }

    @Override
    public User selectUserById(String id) {
        return this.userMapper.selectUserById(id);
    }

    @Override
    public User selectUserByLoginId(String loginId) {
        User user = this.userMapper.selectUserByLoginId(loginId);
        Staff staff = this.staffService.selectStaffByLoginId(loginId);
        user.setStaff(staff);
        return user;
    }

    @Override
    public PageInfo<User> selectUsersByPage(Map params) {
        ServiceUtil.checkPageParams(params);
        PageHelper.startPage(params);
        List<User> users = this.userMapper.selectUserList(params);
        return new PageInfo(users);
    }

    @Override
    public List<User> selectUserList() {
        return this.userMapper.selectUserList(null);
    }

    @Override
    public Boolean checkUser2Role(String loginId, String roleId) {
        return this.userMapper.selectCountByLoginIdAndRoleId(loginId, roleId) > 0 ? true : false;
    }

    @Override
    public Boolean duplicateCheckLoginId(String loginId, String id) {
        return this.userMapper.selectCountByLoginId(loginId, id) == 0 ? true : false;
    }

    @Override
    public Boolean checkLimit2ChangePassword(String loginId) {
        return this.userMapper.selectPasswordLimitIndexByLoginId(loginId) > 0 ? true : false;
    }

    @Override
    public int changeUserPassword(ChangeUserPasswordDTO dto) throws Exception {
        if (this.userMapper.selectPasswordLimitIndexByLoginId(dto.getLoginId()) > 0) {
            return this.userMapper.updateUserPasswordById(dto);
        } else {
            throw new Exception("没有该权限……");
        }
    }

    @Override
    public int changeCurrentUserPassword(ChangeCurrentUserPasswordDTO dto) throws Exception {
        String loginId = dto.getLoginId();
        if (this.userMapper.selectCountByLoginIdAndPassword(loginId, dto.getOldPassword()) > 0) {
            return this.userMapper.updateUserPasswordByLoginId(dto);
        } else {
            throw new Exception("密码错误……");
        }
    }

    @Override
    public List<User> getUserByGroupRole(String groupId, String roleId) {
        return userMapper.getUserByGroupRole(groupId, roleId);
    }

    @Override
    public List<User> getUserByGroup(Long groupId) {
        return userMapper.getUserByGroup(groupId);
    }

    @Override
    public List<User> getUserByRole(Long roleId) {
        return userMapper.getUserByRole(roleId);
    }

    @Override
    public Map queryUserDetailById(String userId) {
        return userMapper.queryUserDetailById(userId);
    }

    @Override
    public PageInfo<Map> queryUserDetailPage(Map params) {
        ServiceUtil.checkPageParams(params);
        PageHelper.startPage(params);
        List<Map<String,Object>> users = userMapper.queryUserDetailPage(params);
        return new PageInfo(users);
    }

    @Override
    public List<User> getUserInfoByIdStrings(String ids) {
        return userMapper.getUserInfoByIdStrings(ids);
    }
}
