//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cxtx.user_manage.web.rest;


import com.cxtx.common.config.jwt.vo.ResponseResult;
import com.cxtx.common.domain.JwtModel;
import com.cxtx.common.unit.HttpServletUtils;
import com.cxtx.user_manage.service.RoleService;
import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.User;
import com.cxtx.user_manage.service.UserService;
import com.cxtx.common.unit.ResponseUtil;
import com.cxtx.user_manage.web.rest.vo.ChangeCurrentUserPasswordDTO;
import com.cxtx.user_manage.web.rest.vo.ChangeUserPasswordDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(
        tags = {"用户管理"},
        description = "管理用户的增删改查"
)
@RestController
@RequestMapping({"/api"})
public class UserResource {
    private final Logger log = LoggerFactory.getLogger(UserResource.class);
    private static final String ENTITY_NAME = "user";
    private static final HashMap<String, String> POWER_MAP = new HashMap();
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    public UserResource() {
    }

    @GetMapping({"/users/check/duplicate"})
    @ApiOperation(
            value = "账号查重",
            notes = "检查账号是否重复",
            response = ResponseUtil.Response.class
    )
    @ApiImplicitParams({@ApiImplicitParam(
            name = "loginId",
            value = "账号",
            required = true,
            paramType = "query",
            dataType = "string"
    ), @ApiImplicitParam(
            name = "id",
            value = "主键，检查时会排除主键等于传入值的记录",
            required = false,
            paramType = "query",
            dataType = "string"
    )})
    public ResponseEntity<Map> duplicateCheckLoginId(@ApiIgnore @RequestParam Map params) {
        try {
            String loginId = params.get("loginId").toString();
            String id = params.get("id") != null ? params.get("id").toString() : null;
            Boolean result = this.userService.duplicateCheckLoginId(loginId, id);
            return result ? ResponseUtil.success("该账号可用") : ResponseUtil.error("账号重复……");
        } catch (Exception var5) {
            return ResponseUtil.error(var5.getMessage());
        }
    }

    @GetMapping({"/users/page"})
    @ApiOperation(
            value = "分页获取用户信息",
            notes = "分页获取用户信息列表",
            response = ResponseUtil.Response.class
    )
    @ApiImplicitParams({@ApiImplicitParam(
            name = "pageNum",
            value = "分页参数：第几页",
            required = true,
            paramType = "query",
            dataType = "int"
    ), @ApiImplicitParam(
            name = "pageSize",
            value = "分页参数：每页数量",
            required = true,
            paramType = "query",
            dataType = "int"
    ), @ApiImplicitParam(
            name = "loginId",
            value = "账号",
            required = false,
            paramType = "query",
            dataType = "string"
    )})
    public ResponseEntity<Map> queryUsersByPage(@ApiIgnore @RequestParam Map params) {
        try {
            PageInfo<User> result = this.userService.selectUsersByPage(params);
            return result != null ? ResponseUtil.success(result) : ResponseUtil.error("未查询到数据……");
        } catch (Exception var3) {
            return ResponseUtil.error(var3.getMessage());
        }
    }

    @GetMapping({"/users/list"})
    public ResponseEntity<Map> queryUsersList() {
        try {
            List<User> result = this.userService.selectUserList();
            return ResponseUtil.success(result);
        } catch (Exception var3) {
            return ResponseUtil.error(var3.getMessage());
        }
    }

    @ApiOperation(
            value = "保存用户",
            notes = "保存用户信息,新增和修改",
            response = ResponseUtil.Response.class
    )
    @ApiImplicitParam(
            name = "user",
            value = "用户信息",
            required = true,
            paramType = "body",
            dataType = "User"
    )
    @PostMapping({"/users"})
    public ResponseEntity<Map> createUser(@Valid @RequestBody User user) throws URISyntaxException {
        try {
            if (user.getLoginId() != null && !"".equals(user.getLoginId())) {
                int result = this.userService.saveUser(user);
                return result > 0 ? ResponseUtil.success(user) : ResponseUtil.error("保存失败……");
            } else {
                return ResponseUtil.error("缺少参数");
            }
        } catch (Exception var3) {
            return ResponseUtil.error(var3.getMessage());
        }
    }

    @ApiOperation(
            value = "修改默认角色",
            notes = "修改当前登陆用户的默认角色",
            response = ResponseUtil.Response.class
    )
    @PostMapping({"/users/current/roles/default/{roleId}/{loginId}"})
    public ResponseEntity<Map> updateUserDefaultRole(@PathVariable String roleId, @PathVariable String loginId) throws URISyntaxException {
        try {
            int result = this.userService.updateUserDefaultRole(loginId, roleId);
            return result > 0 ? ResponseUtil.success("修改成功") : ResponseUtil.error("修改失败……");
        } catch (Exception var4) {
            return ResponseUtil.error(var4.getMessage());
        }
    }

    @ApiOperation(
            value = "获取用户信息",
            notes = "根据主键 获取一个用户的信息",
            response = ResponseUtil.Response.class
    )
    @ApiImplicitParam(
            name = "id",
            value = "id",
            required = true,
            paramType = "path"
    )
    @GetMapping({"/users/{id}"})
    public ResponseEntity<Map> queryUser(@PathVariable String id) {
        try {
            User result = this.userService.selectUserById(id);
            return result != null ? ResponseUtil.success(result) : ResponseUtil.error("获取失败！");
        } catch (Exception var3) {
            return ResponseUtil.error(var3.getMessage());
        }
    }

    @ApiOperation(
            value = "获取用户信息",
            notes = "根据账号 获取一个用户的信息",
            response = ResponseUtil.Response.class
    )
    @GetMapping({"/users/set/{loginId}"})
    public ResponseEntity<Map> queryUserByLoginId(@PathVariable String loginId) {
        try {
            User result = this.userService.selectUserByLoginId(loginId);
            return result != null ? ResponseUtil.success(result) : ResponseUtil.error("获取失败！");
        } catch (Exception var3) {
            return ResponseUtil.error(var3.getMessage());
        }
    }

    @ApiOperation(
            value = "删除用户信息",
            notes = "根据主键 删除一个用户信息",
            response = ResponseUtil.Response.class
    )
    @ApiImplicitParam(
            name = "id",
            value = "id",
            required = true,
            paramType = "path"
    )
    @DeleteMapping({"/users/{id}"})
    public ResponseEntity<Map> deleteUser(@PathVariable String id) {
        try {
            int result = this.userService.deleteUserById(id);
            return result > 0 ? ResponseUtil.success("删除成功！") : ResponseUtil.error("删除失败！");
        } catch (Exception var3) {
            return ResponseUtil.error(var3.getMessage());
        }
    }

    @ApiOperation(
            value = "ONE",
            notes = "获取当前登陆用户信息",
            response = ResponseUtil.Response.class
    )
    @GetMapping({"/users/current/{loginId}"})
    public ResponseEntity<Map> getCurrentUser(@PathVariable String loginId) {
        try {
            User result = this.userService.selectUserByLoginId(loginId);
            return result != null ? ResponseUtil.success(result) : ResponseUtil.error("查询失败！");
        } catch (Exception var3) {
            return ResponseUtil.error(var3.getMessage());
        }
    }

    @ApiOperation(
            value = "检查修改密码的权限",
            notes = "检查是否具有修改其他用户的修改密码的权限",
            response = ResponseUtil.Response.class
    )
    @GetMapping({"/users/password/change/other/check/{loginId}"})
    public ResponseEntity<Map> checkLimit2ChangePassword(@PathVariable String loginId) {
        try {
            Boolean result = this.userService.checkLimit2ChangePassword(loginId);
            return result ? ResponseUtil.success("可以修改……") : ResponseUtil.error("没有该权限……");
        } catch (Exception var3) {
            return ResponseUtil.error(var3.getMessage());
        }
    }

    @ApiOperation(
            value = "修改密码",
            notes = "修改某个用户的密码，会先判断是否有修改密码的权限，可在系统参数表配置权限",
            response = ResponseUtil.Response.class
    )
    @PostMapping({"/users/password/change/other"})
    public ResponseEntity<Map> changePassword(@RequestBody ChangeUserPasswordDTO dto) {
        try {
            int result = this.userService.changeUserPassword(dto);
            return result > 0 ? ResponseUtil.success("修改成功……") : ResponseUtil.error("修改失败……");
        } catch (Exception var3) {
            return ResponseUtil.error(var3.getMessage());
        }
    }

    @ApiOperation(
            value = "修改密码",
            notes = "修改当前登陆用户的密码，修改自己的密码",
            response = ResponseUtil.Response.class
    )
    @PostMapping({"/users/password/change"})
    public ResponseEntity<Map> changeCurrentPassword(@RequestBody ChangeCurrentUserPasswordDTO dto) {
        try {
            int result = this.userService.changeCurrentUserPassword(dto);
            return result > 0 ? ResponseUtil.success("修改成功！") : ResponseUtil.error("修改失败！");
        } catch (Exception var3) {
            return ResponseUtil.error(var3.getMessage());
        }
    }

    @GetMapping(value = "/vue/current")
    public ResponseEntity<Map> current() {
        JwtModel jwtModel = HttpServletUtils.getUserInfo();
        List<String> roleList = roleService.selectRoleListByUserId(jwtModel.getUserId().toString());
        HashMap result = new HashMap(4);
        result.put("roles",roleList.toArray());
        result.put("introduction","I am a super administrator");
        result.put("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        result.put("name","Super Admin");
        return ResponseUtil.success(result);
    }
}
