

package com.cxtx.user_manage.web.rest;

import com.cxtx.common.config.jwt.vo.ResponseResult;
import com.cxtx.common.domain.JwtModel;
import com.cxtx.common.unit.HttpServletUtils;
import com.cxtx.user_manage.domain.Menu;
import com.cxtx.user_manage.service.MenuService;
import com.cxtx.common.unit.ResponseUtil;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(
        tags = {"菜单管理"},
        description = "管理菜单的增删改查"
)
@RestController
@RequestMapping({"/api"})
public class MenusResource {
    private final Logger log = LoggerFactory.getLogger(MenusResource.class);
    @Autowired
    private MenuService menuService;

    public MenusResource() {
    }

    @ApiOperation(
            value = "删除菜单",
            notes = "删除菜单信息",
            response = ResponseUtil.Response.class
    )
    @DeleteMapping({"/menus/{id}"})
    public ResponseEntity<Map> deleteMenus(@PathVariable String id) throws URISyntaxException {
        try {
            int result = this.menuService.deleteMenuById(id);
            return result >= 0 ? ResponseUtil.success("删除成功") : ResponseUtil.error("删除失败");
        } catch (Exception var3) {
            return ResponseUtil.error(var3.getMessage());
        }
    }

    @ApiOperation(
            value = "保存菜单",
            notes = "保存菜单信息",
            response = ResponseUtil.Response.class
    )
    @ApiImplicitParam(
            name = "menu",
            value = "菜单信息",
            required = true,
            paramType = "body",
            dataType = "Menu"
    )
    @PostMapping({"/menus"})
    public ResponseEntity<Map> saveMenus(@ApiIgnore @Valid @RequestBody Menu menu) throws URISyntaxException {
        try {
            int result = this.menuService.saveMenu(menu);
            return result >= 0 ? ResponseUtil.success(menu) : ResponseUtil.error("保存失败");
        } catch (Exception var3) {
            return ResponseUtil.error(var3.getMessage());
        }
    }

    @GetMapping({"/menus/tree"})
    @ApiOperation(
            value = "获取菜单树",
            notes = "获取jstree所需的数据格式的菜单树",
            response = ResponseUtil.Response.class
    )
    public ResponseEntity<Map> queryMenusTree() {
        try {
            List<Map> result = this.menuService.selectMenusTree();
            return result != null ? ResponseUtil.success(result) : ResponseUtil.error("未查询到结果！");
        } catch (Exception var2) {
            return ResponseUtil.error(var2.getMessage());
        }
    }

    @GetMapping("/queryAllStairMenu")
    @ApiOperation(value = "获取菜单" , notes = "获取所有一级菜单")
    public ResponseEntity<Map> queryAllStairMenu(){
        try {
            List<Menu> all = menuService.queryAllStairMenu();
            return all != null ? ResponseUtil.success(all) : ResponseUtil.error("未查询到数据");
        }catch (Exception e){
            throw e;
        }
    }

    @GetMapping("/menus/{id}")
    @ApiOperation(value = "获取菜单详情" , notes = "")
    public ResponseEntity<Map> queryMenuInfo(@PathVariable String id){
        try {
            Menu menu = menuService.queryMenuInfo(id);
            return menu != null ? ResponseUtil.success(menu) : ResponseUtil.error("未查询到数据");
        }catch (Exception e){
            throw e;
        }
    }

    @GetMapping({"/menus/treeTwo"})
    @ApiOperation(
            value = "获取菜单树",
            notes = "获取element所需的数据格式的菜单树",
            response = ResponseUtil.Response.class
    )
    public ResponseEntity<Map> queryMenusTreeTwo() {
        try {
            List<Map> result = this.menuService.selectMenusTreeTwo();
            return result != null ? ResponseUtil.success(result) : ResponseUtil.error("未查询到结果！");
        } catch (Exception var2) {
            return ResponseUtil.error(var2.getMessage());
        }
    }

    @GetMapping({"/menus/tree/{roleId}"})
    @ApiOperation(
            value = "获取菜单树",
            notes = "获取element所需的数据格式的菜单树",
            response = ResponseUtil.Response.class
    )
    public ResponseEntity<Map> queryRoleMenusTree(@PathVariable String roleId) {
        try {
            List<Map> result = this.menuService.queryRoleMenusTree(roleId);
            return result != null ? ResponseUtil.success(result) : ResponseUtil.error("未查询到结果！");
        } catch (Exception var2) {
            return ResponseUtil.error(var2.getMessage());
        }
    }

    @GetMapping({"/menus/init/{roleId}/{loginId}"})
    @ApiOperation(
            value = "TWO",
            notes = "根据roleId获取初始化菜单",
            response = ResponseUtil.Response.class
    )
    public ResponseEntity<Map> queryMenusInit(@PathVariable String roleId, @PathVariable String loginId) {
        try {
            List<Menu> result = this.menuService.selectMenuModulesByRoleId(roleId,loginId);
            return result != null ? ResponseUtil.success(result) : ResponseUtil.error("未查询到结果！");
        } catch (Exception var3) {
            return ResponseUtil.error(var3.getMessage());
        }
    }

    @GetMapping({"/menus"})
    @ApiOperation(
            value = "获取权限",
            notes = "根据roleId和menuId获取菜单权限信息",
            response = ResponseUtil.Response.class
    )
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "roleId", value = "角色id", required = true, paramType = "query", dataType = "string"),
                    @ApiImplicitParam(name = "menuId", value = "菜单Id", required = true, paramType = "query", dataType = "string"),
                    @ApiImplicitParam(name = "loginId", value = "loginId", required = true, paramType = "query", dataType = "string")
            }
    )
    public ResponseEntity<Map> queryMenusByCurrentUser(@ApiIgnore @RequestParam Map param) {
        try {
            List<Menu> result = this.menuService.selectModuleLimitsByRoleId(param);
            return result != null ? ResponseUtil.success(result) : ResponseUtil.error("未查询到结果！");
        } catch (Exception var4) {
            return ResponseUtil.error(var4.getMessage());
        }
    }

    @ApiOperation(value = "动态路由",notes = "配置vue-element-admin 动态路由")
    @GetMapping(value = "/vue/menus")
    public ResponseEntity<Map> menus() {
        try {
            JwtModel jwtModel = HttpServletUtils.getUserInfo();
            //获取当前用户绑定的所有一级菜单
            List<Menu> stairMenuList = menuService.selectStairMenuByUserId(jwtModel.getUserId().toString());
            List<HashMap> result = new ArrayList<>(4);
            if(stairMenuList != null && stairMenuList.size()>0){
                for (Menu stairMenu : stairMenuList){
                    //组装一级菜单数据格式
                    HashMap firstMenu = new HashMap(5);
                    firstMenu.put("path","/"+stairMenu.getAction());
                    firstMenu.put("component","Layout");
                    firstMenu.put("alwaysShow",true);
                    firstMenu.put("name",stairMenu.getName());
                    HashMap firstMate = new HashMap(4);
                    firstMate.put("title",stairMenu.getName());
                    firstMate.put("icon",stairMenu.getStyle());
                    firstMate.put("roles",stairMenu.getUseRoles().split(","));
                    firstMenu.put("meta",firstMate);
                    if(stairMenu.getId() != null){
                        //获取当前一级菜单下所有二级菜单，子路由
                        List<Menu> childrenMenu = menuService.selectChildrenMenuByParentId(stairMenu.getId());
                        if(childrenMenu != null && childrenMenu.size()>0){
                            List<HashMap> children = new ArrayList<>(4);
                            for (Menu child : childrenMenu){
                                HashMap childMenu = new HashMap(4);
                                childMenu.put("path",child.getAction());
                                childMenu.put("component",stairMenu.getAction()+"/"+child.getAction());
                                childMenu.put("alwaysShow",false);
                                childMenu.put("name",child.getName());
                                HashMap childMate = new HashMap(4);
                                childMate.put("title",child.getName());
                                childMate.put("icon",child.getStyle()!=null?child.getStyle():null);
                                childMenu.put("meta",childMate);
                                if(child.getMenuType() == 1){
                                    //如果是子路由，则隐藏
                                    childMenu.put("hidden",true);
                                    childMate.put("roles",stairMenu.getUseRoles().split(","));
                                }else {
                                    childMate.put("roles",child.getUseRoles()!=null?child.getUseRoles().split(","):new ArrayList<>());
                                }
                                children.add(childMenu);
                            }
                            firstMenu.put("children",children);
                        }
                    }
                    result.add(firstMenu);

                }
            }
            return stairMenuList!=null ? ResponseUtil.success(result):ResponseUtil.error("未查询到结果！");
        }catch (Exception var){
            throw var;
        }
    }
}
