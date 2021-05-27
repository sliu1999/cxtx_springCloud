package com.cxtx.auth_service.service;

import org.springframework.web.bind.annotation.RequestParam;

public interface TestService {

    public String sayHello(String name);

    public String hotPoint(@RequestParam("hotPoint") int hotPoint);

    public String getSysParam(String paramId);
}
