//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cxtx.user_manage.web.rest;

import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.Staff;
import com.cxtx.user_manage.service.StaffService;
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

import java.util.HashMap;
import java.util.Map;

@Api(
        tags = {"员工管理"},
        description = "员工信息管理"
)
@RestController
@RequestMapping({"/api"})
public class StaffResource {
    private static Logger logger = LoggerFactory.getLogger(StaffResource.class);
    private static final String ENTITY_NAME = "staff";
    @Autowired
    private StaffService staffService;

    public StaffResource() {
    }

    @ApiOperation(
            value = "保存员工",
            notes = "保存员工信息",
            response = ResponseUtil.Response.class
    )
    @PostMapping({"/staffs"})
    public ResponseEntity<?> saveStaff(@RequestBody Staff staff) {
        try {
            int result = this.staffService.saveStaff(staff);
            return result > 0 ? ResponseUtil.success("保存成功……") : ResponseUtil.success("保存失败……");
        } catch (Exception var3) {
            return ResponseUtil.error(var3.getMessage());
        }
    }

    @ApiOperation(
            value = "查询员工",
            notes = "查询员工详细信息",
            response = ResponseUtil.Response.class
    )
    @GetMapping({"/staffs/{id}"})
    public ResponseEntity<?> selectStaffById(@PathVariable String id) {
        try {
            Staff result = this.staffService.selectStaffById(id);
            return result != null ? ResponseUtil.success(result) : ResponseUtil.success("未查询到数据……");
        } catch (Exception var3) {
            return ResponseUtil.error(var3.getMessage());
        }
    }

    @ApiOperation(
            value = "删除员工",
            notes = "删除员工及关联账号",
            response = ResponseUtil.Response.class
    )
    @DeleteMapping({"/staffs/{id}"})
    public ResponseEntity<?> deleteStaffById(@PathVariable String id) {
        try {
            int result = this.staffService.deleteStaffById(id);
            return result > 0 ? ResponseUtil.success("删除成功……") : ResponseUtil.success("删除失败……");
        } catch (Exception var3) {
            return ResponseUtil.error(var3.getMessage());
        }
    }

    @GetMapping({"/staffs/page"})
    @ApiOperation(
            value = "分页查询员工",
            notes = "以分页查询的方式获取员工信息列表",
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
    )})
    public ResponseEntity<Map> selectStaffListByPage(@ApiIgnore @RequestParam HashMap<String, String> params) {
        try {
            PageInfo<Staff> result = this.staffService.selectStaffListByPage(params);
            return result != null ? ResponseUtil.success(result) : ResponseUtil.error("未查询到结果！");
        } catch (Exception var3) {
            return ResponseUtil.error(var3.getMessage());
        }
    }

    @GetMapping({"/staffs/check/duplicate"})
    @ApiOperation(
            value = "工号查重",
            notes = "检查工号是否重复",
            response = ResponseUtil.Response.class
    )
    @ApiImplicitParams({@ApiImplicitParam(
            name = "staffNo",
            value = "工号",
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
    public ResponseEntity<Map> duplicateCheckStaffNo(@ApiIgnore @RequestParam Map params) {
        try {
            String staffNo = params.get("staffNo").toString();
            String id = params.get("id") != null ? params.get("id").toString() : null;
            Boolean result = this.staffService.duplicateCheckStaffNo(staffNo, id);
            return result ? ResponseUtil.success("该工号可用") : ResponseUtil.error("工号重复……");
        } catch (Exception var5) {
            return ResponseUtil.error(var5.getMessage());
        }
    }
}
