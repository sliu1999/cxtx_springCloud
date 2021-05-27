package com.cxtx.auth_service.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.cxtx.common.domain.MySysParameter;
import com.cxtx.common.unit.MySysParameterOperate;
import com.cxtx.auth_service.service.TestService;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {


    @Override
    @SentinelResource(value = "sayHello", blockHandler = "blockHandler")
    public String sayHello(String name) {
        return "Hello, " + name;
    }
    // blockHandler 函数，原方法调用被限流/降级/系统保护的时候调用
    public String blockHandler(String id, BlockException ex) {
        return "Hello429";
    }
    public String hotBlockHandler(int hotPoint, BlockException ex) {
        return "hot429";
    }

    @Override
    @SentinelResource(value = "hotPoint",blockHandler = "hotBlockHandler")
    public String hotPoint(int hotPoint) {
        return "hotPoint, " + hotPoint;
    }

    @Override
    public String getSysParam(String paramId) {
        return MySysParameterOperate.getInstance().getSysParameterByName(paramId);
    }


}
