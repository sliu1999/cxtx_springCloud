package com.info33.platform.oa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.info33.platform.oa.entity.OaFormMod;

import java.util.List;
import java.util.Map;

public interface AppService extends IService<OaFormMod> {
	int ifTableExist(String tableKey);

	Map<String,Object> createTable(Map<String, Object> map) throws Exception;
	
	Map<String,Object> createTableByCopy(Map<String, Object> map) throws Exception;

	boolean removeFields(String tableKey, List<Map<String, Object>> removeFieldList) throws Exception;

	boolean updateFields(String tableKey, List<Map<String, Object>> updateFieldList) throws Exception;

	boolean addFields(String tableKey, List<Map<String, Object>> addFieldList) throws Exception;

	boolean addDetails(String tableName, List<Map<String, Object>> addDetails) throws Exception;

	boolean updateDetails(String tableName, List<Map<String, Object>> updateDetails)throws Exception;

	boolean removeDetails(String tableName, List<Map<String, Object>> removeDetails)throws Exception;
	
	boolean operateDetailTable(String tableName, Map<String, Object> dataMap)throws Exception;

	boolean operateTable(String TableName, Map<String, Object> dataMap)throws Exception;

	int getCount(String sql);

	List<Map<String,Object>> getInfoBySql(String sql);

	Map<String,Object> getFormDataByField(Map<String,Object> data);

	List<Map<String,Object>> queryDataBySqlMap(Map<String, Object> map);

	int queryCountBySqlMap(Map<String, Object> dataMap);

//	List<String> getRoleName(String roleIds);

//	List<String> getGroupName(String groupIds);
	/**
	 * 生成流水号
	 * @param raw
	 * @return
	 */
	String serialBuilder(Map<String, Object> raw);

	Long queryRoleIdByUserId(Long userId);

	boolean saveRoleAndMod(Long userId, Long roleId);
	
	void deleteForm(Map<String,Object> map);
	/**
	 *  添加map参数对象到指定的数据库表中，并返回该条数据的ID
	 * @param tableName
	 * @param paramMap
	 * @return
	 */
	Long NamedCUDHoldId(String tableName,Map<String,Object> paramMap);
	
	void NamedCUD(String tableName,Map<String,Object> paramMap);
	
	int CUD(String sql);
	
	Object getObject(String sql,String key);
	
	Map<String,Object> getMap(String sql);
	
	List<Map<String, Object>> getList(String sql);
	
	List<Object> getObjectList(String sql);
	
	boolean ifTableExistInDataBase(String tableName);
	
	boolean ifColumnExistInTable(String table,String column);

	String updateTableKey(String tableKey);
	/**
	 * 根据formId查询指定表中的指定字段信息
	 * @param fields
	 * @param tableName
	 * @param formId
	 * @return
	 */
	Map<String,Object> getFormDataByFieldList(List<String>fields,String tableName,Long formId);
	/**
	 * 根据formId查询明细表中的指定字段信息
	 * @param fields
	 * @param tableName
	 * @param formId
	 * @return
	 */
	List<Map<String,Object>> getFormDataListByFieldList(List<String>fields,String tableName,Long formId);
} 
