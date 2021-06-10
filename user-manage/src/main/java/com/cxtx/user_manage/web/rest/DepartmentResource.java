

package com.cxtx.user_manage.web.rest;

import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.Department;
import com.cxtx.user_manage.service.DepartmentService;
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

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(
        tags = {"部门管理"},
        description = "部门信息表相关接口"
)
@RestController
@RequestMapping({"/api"})
public class DepartmentResource {
    private final Logger log = LoggerFactory.getLogger(DepartmentResource.class);
    private static final String ENTITY_NAME = "department";
    @Autowired
    private DepartmentService departmentService;

    public DepartmentResource() {
    }

    @ApiOperation(
            value = "保存部门",
            notes = "保存部门信息",
            response = ResponseUtil.Response.class
    )
    @PostMapping({"/departments"})
    public ResponseEntity<?> saveDepartment(@RequestBody Department department) throws URISyntaxException {
        try {
            int result = this.departmentService.saveDepartment(department);
            return result > 0 ? ResponseUtil.success(department) : ResponseUtil.error("保存失败");
        } catch (Exception var3) {
            return ResponseUtil.error(var3.getMessage());
        }
    }

    @ApiOperation(
            value = "删除部门",
            notes = "删除部门及子部门",
            response = ResponseUtil.Response.class
    )
    @DeleteMapping({"/departments/{id}"})
    public ResponseEntity<?> deleteDepartment(@PathVariable String id) {
        try {
            int result = this.departmentService.deleteDepartment(id);
            return result > 0 ? ResponseUtil.success("删除成功……") : ResponseUtil.error("删除失败……");
        } catch (Exception var3) {
            return ResponseUtil.error(var3.getMessage());
        }
    }

    @ApiOperation(
            value = "查询部门",
            notes = "查询部门详情",
            response = ResponseUtil.Response.class
    )
    @GetMapping({"/departments/{id}"})
    public ResponseEntity<?> selectDepartmentById(@PathVariable String id) {
        try {
            Department department = this.departmentService.selectDepartmentById(id);
            return department != null ? ResponseUtil.success(department) : ResponseUtil.error("未查询的信息……");
        } catch (Exception var3) {
            return ResponseUtil.error(var3.getMessage());
        }
    }

    @ApiOperation(
            value = "查询部门树",
            notes = "返回jstree适用的数据结构的部门树",
            response = ResponseUtil.Response.class
    )
    @GetMapping({"/departments/tree"})
    public ResponseEntity<?> selectDepartmentTree() {
        try {
            List<Map> result = this.departmentService.selectDepartmentTree();
            return result != null ? ResponseUtil.success(result) : ResponseUtil.error("未查询的信息……");
        } catch (Exception var2) {
            return ResponseUtil.error(var2.getMessage());
        }
    }

    @ApiOperation(
            value = "查询部门树",
            notes = "返回element适用的数据结构的部门树",
            response = ResponseUtil.Response.class
    )
    @GetMapping({"/departments/treeTwo"})
    public ResponseEntity<?> selectDepartmentTreeTwo() {
        try {
            List<Map> result = this.departmentService.selectDepartmentTreeTwo();
            return ResponseUtil.success(result);
        } catch (Exception var2) {
            return ResponseUtil.error(var2.getMessage());
        }
    }


    @ApiOperation(
            value = "查询部门树",
            notes = "返回element适用的数据结构的除了自己的部门树",
            response = ResponseUtil.Response.class
    )
    @GetMapping({"/departments/treeExceptMe"})
    public ResponseEntity<?> treeExceptMe(@RequestParam String departId) {
        try {
            List<Map> result = this.departmentService.treeExceptMe(departId);
            return ResponseUtil.success(result);
        } catch (Exception var2) {
            return ResponseUtil.error(var2.getMessage());
        }
    }
    @ApiOperation(
            value = "查询部门树",
            notes = "返回List<Map>",
            response = ResponseUtil.Response.class
    )
    @GetMapping({"/departments/treeExceptMeList"})
    public ResponseEntity<?> treeExceptMeList(@RequestParam String departId) {
        try {
            List<Map> result = this.departmentService.treeExceptMeList(departId);
            return ResponseUtil.success(result);
        } catch (Exception var2) {
            return ResponseUtil.error(var2.getMessage());
        }
    }

    @ApiOperation(
            value = "部门分页查询",
            notes = "根据上级部门id，分页查询部门信息",
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
            name = "parentId",
            value = "上级部门id",
            required = true,
            dataType = "string",
            paramType = "query"
    )})
    @GetMapping({"/departments/page"})
    public ResponseEntity<?> selectDepartmentListByPage(@ApiIgnore @RequestParam HashMap<String, Object> params) {
        try {
            PageInfo<Department> result = this.departmentService.selectDepartmentListByPage(params);
            return result != null ? ResponseUtil.success(result) : ResponseUtil.error("未查询到结果！");
        } catch (Exception var3) {
            return ResponseUtil.error(var3.getMessage());
        }
    }
}
