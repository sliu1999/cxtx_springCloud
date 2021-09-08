package com.cxtx.common.unit.aop.db;


import com.cxtx.common.domain.SysRestLog;
import com.cxtx.common.service.SysRestLogService;
import com.cxtx.common.unit.MySysParameterOperate;
import com.cxtx.common.unit.ThreadOperate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

@Component
@Order(21)
public class SysRestLogRunner implements ApplicationRunner {

    private static Integer THREAD_SIZE = 5;
    private static String THREAD_NAME_PRE = "QueueThreadConsumer-";
    private static Integer threadPoolCode;
    private static String SYS_PARAM_ID = "REST_LOG_LAUNCH";
    private static String SYS_PARAM_VALUE_LAUNCH = "1";


    @Autowired
    private SysRestLogQueue sysRestLogQueue;

    @Autowired
    private SysRestLogService sysRestLogService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        Boolean launch = isLaunch();
        if (launch) {
            String threadName = THREAD_NAME_PRE + "%d";
            List<Runnable> commands = new ArrayList();
            //线程里放入日志消费对象
            //线程start()时会调用日志消费的run()方法
            for(int i = 0; i < THREAD_SIZE; ++i) {
                commands.add(new SysRestLogConsumer(this.sysRestLogQueue, this.sysRestLogService, THREAD_NAME_PRE + (i + 1)));
            }
            //创建一个线程池，核心和最大线程数5个，任务队列是无界队列
            threadPoolCode = ThreadOperate.runThreads(threadName, commands);
        }
    }
    public static void stopLogRunner() {
        ThreadOperate.removeThreadPool(threadPoolCode);
    }

    public static Boolean isLaunch() {
        return SYS_PARAM_VALUE_LAUNCH.equals(MySysParameterOperate.getInstance().getSysParameterByName(SYS_PARAM_ID));
    }

    @PreDestroy
    public void destory() {
        stopLogRunner();
    }
}
