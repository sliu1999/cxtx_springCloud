package com.cxtx.user_manage.web.rest;

import com.cxtx.common.domain.JwtModel;
import com.cxtx.common.unit.HttpServletUtils;
import com.cxtx.common.unit.ResponseUtil;
import com.cxtx.user_manage.domain.*;
import com.cxtx.user_manage.service.*;
import com.cxtx.user_manage.unit.GuavaUtil;
import com.github.pagehelper.PageInfo;
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

@Api(tags = "Oa")
@RestController
@RequestMapping("/api/oa")
public class OaResource {

    private final Logger log = LoggerFactory.getLogger(OaResource.class);

    private static final String ENTITY_NAME = "oa";

    @Autowired
    private OaFlowService oaFlowService;

    @Autowired
    private OaFormModelService oaFormModelService;

    @Autowired
    private OaFormProcessInstanceService oaFormProcessInstanceService;

    @Autowired
    private OaProcessRunService oaProcessRunService;

    @Autowired
    private OaProcessLogService oaProcessLogService;

    @Autowired
    private OaService oaService;

    @PostMapping("/submit")
    @ApiOperation(value = "流程发起", notes = "流程发起")
    public ResponseEntity<Map> submit(@RequestBody Map<String, Object> payload) {
        try {
            JwtModel curUser = HttpServletUtils.getUserInfo();
            // 获取当前表单模块
            Map<String, Object> module = (Map<String, Object>) payload.get("module");
            Long formModId = Long.parseLong(module.get("id").toString());
            if (null == oaFlowService.selectFlowByForm(formModId)) {
                // 表单没有绑定流程，直接填充数据
                Long formId = oaFormModelService.insertFormData(payload, curUser);
                OaFormProcessInstance oaFormProcessInstance = new OaFormProcessInstance();
                oaFormProcessInstance.setAppFormId(formId);
                oaFormProcessInstance.setFormModelId(formModId);
                oaFormProcessInstance.setUserId(Long.valueOf(curUser.getUserId()));
                oaFormProcessInstanceService.insertSelective(oaFormProcessInstance);
                return ResponseUtil.success("提交数据成功！");
            } else {
                // 发起流程
                Map result = oaService.submit(payload, curUser);
                if("1".equals(result.get("successStatus"))){
                    return ResponseUtil.success(result);
                }else if("2".equals(result.get("successStatus"))){
                    return ResponseUtil.success(2,"请选择审批人",result.get("data"));
                }else {
                    return ResponseUtil.error("失败");
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.error(e.getMessage());
        }
    }


    @GetMapping("/listRunProcess")
    @ApiOperation(value = "待办事项", notes = "待办事项", response = ResponseUtil.Response.class)
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "pageNum", value = "分页参数：第几页", required = true, paramType = "query", dataType = "int"),
                    @ApiImplicitParam(name = "pageSize", value = "分页参数：每页数量", required = true, paramType = "query", dataType = "int")
            }
    )
    public ResponseEntity<Map> getRunProcessList(@ApiIgnore @RequestParam Map params) {
        try {
            JwtModel curUser = HttpServletUtils.getUserInfo();
            params.put("userId",curUser.getUserId());
            PageInfo<Map<String,Object>> result = oaProcessRunService.queryByPage(params);
            if (result != null) {
                return ResponseUtil.success(result);
            } else {
                return ResponseUtil.error("有错误");
            }
        }catch (Exception e){
            return ResponseUtil.error(e.getMessage());
        }
    }

    @GetMapping("/getProcessDetail/{processId}")
    @ApiOperation(value = "待办详情", notes = "待办详情", response = ResponseUtil.Response.class)
    public ResponseEntity<Map> getProcessDetail(@PathVariable Long processId) {
        Map result = oaService.getProcessDetail(processId);
        if (result != null) {
            return ResponseUtil.success(result);
        } else {
            return ResponseUtil.error("获取失败！");
        }
    }


    @GetMapping("/getHisProcessDetail/{processId}")
    @ApiOperation(value = "历史详情", notes = "历史详情", response = ResponseUtil.Response.class)
    public ResponseEntity<Map> getHisProcessDetail(@PathVariable Long processId) {
        Map result = oaService.getHisProcessDetail(processId);
        if (result != null) {
            return ResponseUtil.success(result);
        } else {
            return ResponseUtil.error("获取失败！");
        }
    }

    @PostMapping("/handle")
    @ApiOperation(value = "流程审批-同意", notes = "流程审批-同意", response = ResponseUtil.Response.class)
    public ResponseEntity<Map> handle(@RequestBody Map<String, Object> payload) {
        try {
            JwtModel curUser = HttpServletUtils.getUserInfo();
            Map result = oaService.approval(payload, curUser);
            if("1".equals(result.get("successStatus"))){
                return ResponseUtil.success(result);
            }else if("2".equals(result.get("successStatus"))){
                return ResponseUtil.success(2,"请选择审批人",result.get("data"));
            }else {
                return ResponseUtil.error("失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.error(e.getMessage());
        }
    }

    @PostMapping("/refuse")
    @ApiOperation(value = "流程审批-拒绝", notes = "流程审批-拒绝", response = ResponseUtil.Response.class)
    public ResponseEntity<Map> refuse(@RequestBody Map<String, Object> payload) {
        try {
            JwtModel curUser = HttpServletUtils.getUserInfo();
            Map result = oaService.refuse(payload, curUser);
            if("1".equals(result.get("successStatus"))){
                return ResponseUtil.success(result);
            }else if("2".equals(result.get("successStatus"))){
                return ResponseUtil.success(2,result.get("message").toString(),result.get("data"));
            }else if("-2".equals(result.get("successStatus"))){
                return ResponseUtil.error(result.get("message").toString());
            }
            return ResponseUtil.error("失败");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.error(e.getMessage());
        }
    }

    @PostMapping("/transfer")
    @ApiOperation(value = "流程审批-转交", notes = "流程审批-转交", response = ResponseUtil.Response.class)
    public ResponseEntity<Map> transfer(@RequestBody Map<String, Object> payload) {
        try {
            JwtModel curUser = HttpServletUtils.getUserInfo();
            Map result = oaService.forward(payload, curUser);
            if("-2".equals(result.get("successStatus"))){
                return ResponseUtil.error(result.get("message").toString());
            }else if("-3".equals(result.get("successStatus"))){
                return ResponseUtil.error(result.get("message").toString());
            }else {
                return ResponseUtil.success("转交成功",result);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.error(e.getMessage());
        }
    }

    @DeleteMapping("/deleteProcess/{processIds}")
    public ResponseEntity<Map> deleteProcessBatch(@PathVariable("processIds") String processIds) {
        try {
            List<String> processIdList = GuavaUtil.split2list(",", processIds);
            for (String id : processIdList) {
                oaService.deleteProcess(Long.parseLong(id));
            }
            return ResponseUtil.success("流程删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.error(e.getMessage());
        }
    }

}
