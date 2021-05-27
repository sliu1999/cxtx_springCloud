package com.cxtx.auth_service.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TestDel {

    public void doTask(){
        System.out.print("测试定时任务");


    }
}
