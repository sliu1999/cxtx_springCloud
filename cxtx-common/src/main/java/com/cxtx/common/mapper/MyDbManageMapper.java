package com.cxtx.common.mapper;



public interface MyDbManageMapper {

    int createDbLogTable();

    int selectDbLogByFileName(String var1);

    int insertDbLog(String var1);
}
