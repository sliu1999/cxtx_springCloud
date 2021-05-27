package com.cxtx.gateway.web.rest;

import com.cxtx.gateway.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DefaultHystrixController {

    @RequestMapping("/defaultfallback")
    public ResponseEntity<Map> defaultfallback(){
        System.out.println("降级操作...");
        Map<String,String> map = new HashMap<>();
        map.put("resultCode","fail");
        map.put("resultMessage","服务异常");
        map.put("resultObj","null");
        return ResponseUtil.error(503,"服务不可用");
    }
}

