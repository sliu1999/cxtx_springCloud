package com.cxtx.sce_manage.web.rest;

import com.cxtx.sce_manage.service.TestService;
import com.cxtx.sce_manage.web.client.thirdparty.AMapClient;
import com.cxtx.sce_manage.web.client.thirdparty.scenicClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Sentinel限流
 * @author sliu
 * @date 2021/3/29
 */
@RestController
@RequestMapping("/api")
public class XianLiuResource {

    @Autowired
    private TestService testservice;

    @Autowired
    private AMapClient aMapClient;
    @Autowired
    private scenicClient scenicClient;

    @GetMapping(value = "/hello")
    public String apiHello() {
        return testservice.sayHello("hello");
    }


    /**
     * 热点限流，根据参数值限流
     * @return
     */
    @GetMapping(value = "/hotPoint")
    public String hotPoint(@RequestParam("hotPoint") int hotPoint) {
        return testservice.hotPoint(hotPoint);
    }

    @GetMapping(value = "/getSysParam")
    public String getSysParam(@RequestParam("paramId") String paramId) {
        return testservice.getSysParam(paramId);
    }

    @GetMapping(value = "/geocode")
    public void geocode() {
        String time = String.valueOf(System.currentTimeMillis()/ 1000);
        System.out.println(time);
        String secretTime = "ClT6JysstSZC0RqesSh4" + time;
        String md5Str = DigestUtils.md5DigestAsHex(secretTime.getBytes());
        System.out.println(md5Str);
        Map<String,Object> reqJsonStr = new HashMap<String, Object>(3);
        reqJsonStr.put("key","M1JINJQ9");
        reqJsonStr.put("timestamp",time);
        reqJsonStr.put("sign",md5Str);

        Map result = scenicClient.getResource(reqJsonStr);
        System.out.println(result);
    }

    @GetMapping("/timeout")
    public String timeout(){
        try{
            //睡5秒，网关Hystrix3秒超时，会触发熔断降级操作
            Thread.sleep(4000);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "timeout";
    }


}