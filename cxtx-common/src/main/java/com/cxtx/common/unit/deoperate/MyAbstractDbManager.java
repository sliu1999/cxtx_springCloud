package com.cxtx.common.unit.deoperate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cxtx.common.mapper.MyDbManageMapper;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;

import javax.sql.DataSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public abstract class MyAbstractDbManager {
    protected static Logger logger = LoggerFactory.getLogger(MyAbstractDbManager.class);
    private String driverKeyMysql = "MYSQL";
    private String driverKeyOracle = "ORACLE";
    protected String rootDir = "db-config";
    protected String changeLogDir = "changelog";
    protected String changeLogFile = "changelog.json";
    private String fileSeparator;
    private ClassLoader classLoader;
    private String dataBaseType;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private MyDbManageMapper dbManageMapper;

    public MyAbstractDbManager() {
        this.fileSeparator = File.separator;
        this.classLoader = MyAbstractDbManager.class.getClassLoader();
    }

    public void run(ApplicationArguments args) throws Exception {
        logger.info("数据库检查……");
        this.initDataBaseInfo();
        logger.info("数据库类型：" + this.dataBaseType);

        logger.info("创建数据库版本日志表……");
        this.dbManageMapper.createDbLogTable();



        String dir = this.dataBaseType.toLowerCase();
        this.initDir(dir, this.fileSeparator);
        String updatesqlPath = this.rootDir + "/" + this.changeLogDir + "/" + dir;
        String changeLogJsonPath = this.rootDir + "/" + this.changeLogDir + "/" + this.changeLogFile;
        logger.info("changeLogPath : {}", updatesqlPath);
        logger.info("configJsonPath : {}", changeLogJsonPath);
        InputStream changeLogIn = this.classLoader.getResourceAsStream(changeLogJsonPath);
        if(changeLogIn == null){
            logger.info("没有需要进行数据库初始化的文件");
            return;
        }
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        int length;
        while((length = changeLogIn.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }

        String str = result.toString(StandardCharsets.UTF_8.name());
        JSONObject changeLogJson = JSONObject.parseObject(str);
        JSONArray logs = changeLogJson.getJSONObject(dir).getJSONArray("logs");

        for(int i = 0; i < logs.size(); ++i) {
            JSONObject log = logs.getJSONObject(i);
            String fileName = log.getString("file");
            logger.info("发现sql文件:" + fileName);
            if (this.dbManageMapper.selectDbLogByFileName(fileName) == 0) {
                logger.info("执行sql文件:" + fileName);
                this.runSqlFile(this.classLoader.getResourceAsStream(updatesqlPath + '/' + fileName));
                this.dbManageMapper.insertDbLog(fileName);
            }
        }

    }

    private void runSqlFile(File sqlFile) {
        try {
            Connection connection = this.dataSource.getConnection();
            ScriptRunner runner = new ScriptRunner(connection);
            runner.setDelimiter("##");
            runner.runScript(new InputStreamReader(new FileInputStream(sqlFile.getPath()), "UTF-8"));
            connection.close();
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    private void runSqlFile(InputStream sqlInput) {
        try {
            Connection connection = this.dataSource.getConnection();
            ScriptRunner runner = new ScriptRunner(connection);
            runner.setDelimiter("##");
            runner.runScript(new InputStreamReader(sqlInput, "UTF-8"));
            connection.close();
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    private void initDir(String dbDir, String separator) {
        String dbChangeLogDir = this.rootDir + separator + this.changeLogDir + separator + dbDir;
        if (this.classLoader.getResource(this.rootDir) == null) {
            FileUtil.newFolder(this.classLoader.getResource("").getPath() + this.rootDir);
        }

        if (this.classLoader.getResource(dbChangeLogDir) == null) {
            FileUtil.newFolder(this.classLoader.getResource("").getPath() + dbChangeLogDir);
        }

    }

    private void initDataBaseInfo() throws SQLException {
        Connection connection = this.dataSource.getConnection();
        DatabaseMetaData data = connection.getMetaData();
        logger.info("数据库驱动：" + connection.getMetaData().getDriverName().toUpperCase());
        if (data.getDriverName().toUpperCase().indexOf(this.driverKeyMysql) != -1) {
            this.dataBaseType = this.driverKeyMysql;
        } else if (data.getDriverName().toUpperCase().indexOf(this.driverKeyOracle) != -1) {
            this.dataBaseType = this.driverKeyOracle;
        }

    }
}
