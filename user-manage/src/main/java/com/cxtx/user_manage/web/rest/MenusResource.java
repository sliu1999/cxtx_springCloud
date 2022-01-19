

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


    @ApiOperation(value = "动态路由",notes = "配置vue-element-admin 动态路由")
    @GetMapping(value = "/vue/menus")
    public ResponseEntity<Map> menus() {
        try {
            List<Menu> firstMenuList = new ArrayList<>();
            JwtModel jwtModel = HttpServletUtils.getUserInfo();
            List<Menu> all = menuService.getAllByUserId(jwtModel.getUserId().toString());
            for (Menu firstMenus : all){
                if(firstMenus.getParentId() == null){
                    firstMenuList.add(firstMenus);
                }
            }
            List<HashMap> result = new ArrayList<>();
            for (Menu firstMenus : firstMenuList){
                //组装一级菜单数据格式
                HashMap firstMenu = new HashMap(5);
                firstMenu.put("path",firstMenus.getPath());
                firstMenu.put("component","Layout");
                firstMenu.put("alwaysShow",true);
                firstMenu.put("redirect",firstMenus.getRedirect());
                firstMenu.put("name",firstMenus.getName());
                HashMap firstMate = new HashMap(4);
                firstMate.put("title",firstMenus.getTitle());
                firstMate.put("icon",firstMenus.getIcon());
                firstMate.put("roles",firstMenus.getUseRoles().split(","));
                firstMenu.put("meta",firstMate);
                List<HashMap> firstChildren = new ArrayList<>();
                //组装二级菜单数据格式
                for (Menu secondMenus : all){
                    if(firstMenus.getId().equals(secondMenus.getParentId())){
                        HashMap secondMenu = new HashMap(5);
                        secondMenu.put("path",secondMenus.getPath());
                        secondMenu.put("component",secondMenus.getComponent());
                        secondMenu.put("name",secondMenus.getName());
                        secondMenu.put("redirect",secondMenus.getRedirect());
                        HashMap secondMate = new HashMap(4);
                        secondMate.put("title",secondMenus.getTitle());
                        secondMate.put("icon",secondMenus.getIcon());
                        secondMate.put("roles",secondMenus.getUseRoles().split(","));
                        secondMenu.put("meta",secondMate);
                        List<HashMap> secondChildren = new ArrayList<>();
                        //组装三级菜单数据格式
                        for (Menu threeMenus : all){
                            if(secondMenus.getId().equals(threeMenus.getParentId())){
                                HashMap threeMenu = new HashMap(5);
                                threeMenu.put("path",threeMenus.getPath());
                                threeMenu.put("component",threeMenus.getComponent());
                                threeMenu.put("hidden",true);
                                threeMenu.put("name",threeMenus.getName());
                                HashMap threeMate = new HashMap(4);
                                threeMate.put("title",threeMenus.getTitle());
                                threeMate.put("icon",threeMenus.getIcon());
                                threeMate.put("roles",threeMenus.getUseRoles().split(","));
                                threeMenu.put("meta",threeMate);
                                secondChildren.add(threeMenu);
                            }
                        }
                        secondMenu.put("children",secondChildren);
                        firstChildren.add(secondMenu);
                    }
                }
                firstMenu.put("children",firstChildren);
                result.add(firstMenu);
            }

            return result!=null ? ResponseUtil.success(result):ResponseUtil.error("未查询到结果！");
        }catch (Exception var){
            throw var;
        }
    }
}
