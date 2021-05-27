package com.cxtx.sce_manage.web.client.thirdparty;


import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@FeignClient(url = scenicClient.BASE_URL, name = "scenicapi")
public interface scenicClient {

    public static final String BASE_URL = "https://czktest.niulixin.com/";


    @PostMapping("api/resource/openResource")
    Map getResource(@QueryMap Map<String, Object> queryMap);


}
