package com.cxtx.common.unit.deoperate;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(22)
public class MyCommonDbManager extends MyAbstractDbManager implements ApplicationRunner {
    private static Logger logger = LoggerFactory.getLogger(MyCommonDbManager.class);

    public MyCommonDbManager() {
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("初始化数据库配置……");
        this.rootDir = "db-config";
        this.changeLogDir = "changelog";
        this.changeLogFile = "changelog.json";
        super.run(args);
    }
}
