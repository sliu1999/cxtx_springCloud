package com.cxtx.user_manage.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AppMapper{


	void createTable(@Param("map") Map<String, Object> map);

	void removeFields(@Param("map") Map<String, Object> removeFieldList);
	/**
	 * 删除单个数据库表字段
	 * @param tableName
	 * @param id
	 */
	void removeField(@Param("tableName") String tableName, @Param("id") String id);

	void updateFields(@Param("map") Map<String, Object> updateFieldList);

	void addFields(@Param("map") Map<String, Object> addFieldList);
	/**
	 * 新增单个数据库表字段
	 * @param tableName
	 * @param id
	 * @param ctrl
	 */
	void addField(@Param("tableName") String tableName, @Param("id") String id, @Param("ctrl") String ctrl);

	void addDetails(@Param("map") Map<String, Object> addDetails);

	void removeDetails(@Param("map") Map<String, Object> removeDetails);

	int getCount(@Param("sql") String sql);

	List<Map<String,Object>> getInfoBySql(@Param("sql") String sql);

	Map<String, Object> getFormDataByField(@Param("sql") String sql);

	List<Map<String, Object>> queryData(Map<String, Object> map);

	int queryCount(Map<String, Object> map);

	Long queryRoleIdByUserId(Long userId);

	void deleteForm(@Param("map") Map<String, Object> map);


	/**
	 * 将map的key作为数据库字段，value作为插入的参数
	 * @param tableName 表名
	 * @param paramMap map参数
	 * @return
	 */
	Long NamedCUDHoldId(@Param("tableName") String tableName, @Param("param") Map<String, Object> paramMap);
	/**
	 * 根据主键ID将指定数据库表的数据更新
	 * @param tableName 表名
	 * @param paramMap map参数
	 */
	Long NamedCUD(@Param("tableName") String tableName, @Param("param") Map<String, Object> paramMap);

	void CUD(@Param("sql") String sql);

	Map<String,Object> getObject(@Param("sql") String sql);

	Map<String,Object> getMap(@Param("sql") String sql);

	List<Map<String, Object>> getList(@Param("sql") String sql);

	List<Object> getObjectList(@Param("sql") String sql);
	/**
	 * sliu
	 * 获取当前数据库名称
	 * @return
	 */
	String getDataBaseName();
	/**
	 * sliu
	 * 判断数据库中是否存在数据库表
	 * @param
	 * @return
	 */
	int ifTableExistInDataBase(@Param("dataBaseName") String dataBaseName, @Param("tableName") String tableName);

	/**
	 * 判断新增字段在数据库表中是否已存在
	 * @param dataBase
	 * @param table
	 * @param column
	 * @return
	 */
	int ifColumnExistInTable(@Param("dataBase") String dataBase, @Param("table") String table, @Param("column") String column);

	Map<String,Object> getFormDataByFieldList(@Param("fields") List<String> fields, @Param("tableName") String tableName, @Param("id") Long formId);

	List<Map<String,Object>> getFormDataListByFieldList(@Param("fields") List<String> fields, @Param("tableName") String tableName, @Param("id") Long formId);

	List<Map<String,String>> getTableColumnList(@Param("dataBase") String dataBase, @Param("tableName") String tableName);
}
