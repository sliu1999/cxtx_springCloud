//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cxtx.user_manage.web.rest;

import com.cxtx.common.unit.ResponseUtil;
import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.Role;
import com.cxtx.user_manage.service.RoleService;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(
        tags = {"角色管理"},
        description = "管理角色的增删改查"
)
@RestController
@RequestMapping({"/api"})
public class RoleResource {
    private final Logger log = LoggerFactory.getLogger(RoleResource.class);
    private static final String ENTITY_NAME = "role";
    @Autowired
    private RoleService roleService;

    public RoleResource() {
    }

    @GetMapping({"/roles/page"})
    @ApiOperation(
            value = "分页查询角色",
            notes = "以分页查询的方式获取角色信息列表",
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
            name = "name",
            value = "角色名称",
            required = false,
            paramType = "query",
            dataType = "string"
    )})
    public ResponseEntity<Map> queryRolesByPage(@ApiIgnore @RequestParam HashMap<String, String> params) {
        try {
            PageInfo<Role> result = this.roleService.selectRolesByPage(params);
            return result != null ? ResponseUtil.success(result) : ResponseUtil.error("未查询到结果！");
        } catch (Exception var3) {
            return ResponseUtil.error(var3.getMessage());
        }
    }

    @PostMapping({"/roles"})
    @ApiOperation(
            value = "保存角色",
            notes = "保存角色信息",
            response = ResponseUtil.Response.class
    )
    @ApiImplicitParam(
            name = "role",
            value = "角色和角色权限列表",
            required = true,
            paramType = "body",
            dataType = "Role"
    )
    public ResponseEntity<Map> saveRole(@ApiIgnore @Valid @RequestBody Role role) {
        try {
            int result = this.roleService.saveRole(role);
            return result > 0 ? ResponseUtil.success("保存成功") : ResponseUtil.error("没有需要保存的数据！");
        } catch (Exception var3) {
            return ResponseUtil.error(var3.getMessage());
        }
    }

    @GetMapping({"/roles/{id}"})
    @ApiOperation(
            value = "查询角色",
            notes = "根据主键查询角色",
            response = ResponseUtil.Response.class
    )
    public ResponseEntity<Map> selectRoleById(@PathVariable String id) {
        try {
            Role result = this.roleService.selectRoleById(id);
            return result != null ? ResponseUtil.success(result) : ResponseUtil.error("未查询的角色信息！");
        } catch (Exception var3) {
            return ResponseUtil.error(var3.getMessage());
        }
    }

    @DeleteMapping({"/roles/{id}"})
    @ApiOperation(
            value = "删除角色",
            notes = "根据主键删除角色",
            response = ResponseUtil.Response.class
    )
    public ResponseEntity<Map> deleteRoleById(@PathVariable String id) {
        try {
            int result = this.roleService.deleteRoleById(id);
            return result > 0 ? ResponseUtil.success(result) : ResponseUtil.error("没有需要删除的数据！");
        } catch (Exception var3) {
            return ResponseUtil.error(var3.getMessage());
        }
    }

    @GetMapping("/getAllRole")
    @ApiOperation(value = "获取所有角色列表",notes = "获取角色列表",response = ResponseUtil.Response.class)
    public ResponseEntity<Map> selectAllRole(){
        try {
            List<Role> result = roleService.selectAllRole();
            return result != null ? ResponseUtil.success(result) : ResponseUtil.error("没有需要查询的数据");
        }catch (Exception e){
            return ResponseUtil.error(e.getMessage());
        }
    }
}
