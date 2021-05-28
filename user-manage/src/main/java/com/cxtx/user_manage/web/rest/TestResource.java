package com.cxtx.user_manage.web.rest;


import com.cxtx.user_manage.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api")
public class TestResource {

    @Autowired
    private TestService testservice;

    @GetMapping(value = "/hello")
    public String apiHello() {
        return "hello";
    }


    @GetMapping(value = "/testTryEx")
    public String testTryEx() {
        try {
            int i = 1/0;
        }catch (Exception e){
            throw e;
        }
        return "hello";
    }

    @GetMapping(value = "/testEx")
    public String testEx() {
        int i = 1/0;
        return "hello";
    }
}