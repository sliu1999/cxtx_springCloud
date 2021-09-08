package com.cxtx.common.web.rest;

import com.cxtx.common.domain.SysDictionary;
import com.cxtx.common.unit.ResponseUtil;
import com.cxtx.common.unit.ThreadPoolExecutorUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

@RestController
@RequestMapping({"/api"})
public class ThreadPoolTestResource {

    @GetMapping(value = "/testThreadPool")
    public  void test(){
        //创建线程池
        List<HashMap> menuList = new ArrayList<>();
        final CountDownLatch count = new CountDownLatch(menuList.size());
        final int[] successNum = {0};
        ThreadPoolExecutor pool = ThreadPoolExecutorUtil.get();
        for (HashMap data : menuList){

            pool.execute(new Runnable() {
                @Override
                public void run() {
                    try {

                        successNum[0]++;
                    }catch (Exception e){
                        e.getStackTrace();
                    }finally {
                        count.countDown();
                    }
                }
            });
        }
        try {
            count.await(); //等待线程结束
        }catch (Exception e){}

        //关闭线程池
        pool.shutdown();
        ThreadPoolExecutor tpe = ((ThreadPoolExecutor) pool);
        int queueSize = tpe.getQueue().size();
        System.out.println("当前排队线程数：" + queueSize);
        int activeCount = tpe.getActiveCount();
        System.out.println("当前活动线程数：" + activeCount);
        long completedTaskCount = tpe.getCompletedTaskCount();
        System.out.println("执行完成线程数：" + completedTaskCount);
        long taskCount = tpe.getTaskCount();
        System.out.println("总线程数：" + taskCount);
    }

}
