package com.cxtx.user_manage.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cxtx.user_manage.domain.OaFormModel;
import com.cxtx.user_manage.mapper.AppMapper;
import com.cxtx.user_manage.mapper.OaFormModelMapper;
import com.cxtx.user_manage.service.AppService;
import com.cxtx.user_manage.unit.GuavaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service("AppService")
public class AppServiceImpl implements AppService {
	@Autowired
	private AppMapper appMapper;
	@Resource
	private OaFormModelMapper oaFormModelMapper;
	@Resource(name = "transactionManager")
	private DataSourceTransactionManager transactionManager;
	@Override
	public int ifTableExist(String tableKey) {
		return appMapper.ifTableExist(tableKey);
	}
	/**
	 * sliu
	 * 建表
	 * @throws Exception 
	 */
	@Override
	@Transactional(rollbackFor = Exception.class )
	public Map<String,Object> createTable(Map<String,Object> map) throws Exception {
		try {
			appMapper.createTable(map);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("创建表失败！");
		}
	}
	/**
	 * 复制表模型建表
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackFor = Exception.class )
	public Map<String,Object> createTableByCopy(Map<String, Object> map) throws Exception{
		Map<String,Object> fieldMap = new HashMap<String,Object>();
		//被复制表单的modId
		Long copiedModId = Long.valueOf(map.get("copiedModId").toString());
		//根据modId来查询sys_mod表中的mod信息
		OaFormModel copyModEntity = oaFormModelMapper.selectByPrimaryKey(copiedModId);
		String tableSchema = copyModEntity.getTableSchema();
		String formView = copyModEntity.getFormView();
		String detailKeys = copyModEntity.getDetailKeys();
		String tableName = "_app_"+map.get("tableKey").toString();
		List<Map<String,Object>> fields = (List<Map<String, Object>>) JSONObject.parse(tableSchema);
		List<Map<String,Object>> fieldList = new ArrayList<Map<String,Object>>();
		fieldMap.put("tableKey", map.get("tableKey").toString());
		try {
			
			//遍历字段是否是明细表字段
			for(Map<String,Object> field:fields) {
				List<Map<String,Object>> details = new ArrayList<Map<String,Object>>();
				boolean flagDetail = field.get("flagDetail")==null?false:true;
				if(flagDetail) {
					details.add(field);
					//创建明细表
					addDetails(tableName, details);
				}else {
					//将主表字段添加到list集合中，除去明细表字段
					fieldList.add(field);
				}
			}
			fieldMap.put("fieldList", GuavaUtil.turnCtrl(fieldList));
			//创建主表
			this.createTable(fieldMap);
			map.put("tableSchema", tableSchema);
			map.put("formView", formView);
			map.put("detailKeys", detailKeys);
			return map;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	//移除主表字段
	@Override
	@Transactional(rollbackFor = Exception.class )
	public boolean removeFields(String tableKey, List<Map<String, Object>> removeFieldList) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		boolean flag = true;
		map.put("tableKey", tableKey);
		List<Map<String,Object>> convertField =  GuavaUtil.turnCtrl(removeFieldList);
		List<Map<String,Object>> operatedField =  new ArrayList<Map<String,Object>>();
		map.put("fieldList", convertField);
		String fieldId = "";
		String fieldName = "";
		try {
			for(Map<String,Object> field:convertField) {
				fieldId = (String) field.get("id");
				fieldName = (String) field.get("name");
				//判断字段是否存在，存在则进行删除操作
				if(ifColumnExistInTable(tableKey, fieldId)) {
					appMapper.removeField(tableKey, fieldId);
					operatedField.add(field);
				}
			}
		} catch (Exception e) {
			//出现异常，将之前删掉的字段添加回去
			addFields(tableKey, operatedField);
			throw new Exception("删除主表字段名:"+fieldName+",字段ID:"+fieldId+"失败！");
		}
		return flag;
	}
	/**
	 * 更新主表字段
	 * @throws Exception 
	 */
	@Override
	@Transactional(rollbackFor = Exception.class )
	public boolean updateFields(String tableKey, List<Map<String, Object>> updateFieldList) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		boolean flag = true;
		map.put("tableKey", tableKey);
		map.put("fieldList", GuavaUtil.turnCtrl(updateFieldList));
		try {
			appMapper.updateFields(map);
			return flag;
		} catch (Exception e) {
			throw new Exception("更新主表字段失败！");
		}
	}
	/**
	 * 添加表字段
	 * @throws Exception 
	 */
	@Override
	@Transactional(rollbackFor = Exception.class )
	public boolean addFields(String tableKey, List<Map<String, Object>> addFieldList) throws Exception {
		
		boolean flag = true;
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("tableKey", tableKey);
		List<Map<String,Object>> convertField =  GuavaUtil.turnCtrl(addFieldList);
		map.put("fieldList", convertField);
		String fieldId = "";
		String fieldName = "";
		String fieldCtrl = "";
		//记录已操作的表字段
		Map<String,Object> operatedField = new HashMap<String,Object>();
		List<Map<String,Object>> operatedFieldList = new ArrayList<Map<String,Object>>();
		operatedField.put("tableKey", tableKey);
		for(Map<String,Object> field:convertField) {
			try {
				fieldId = (String) field.get("id");
				fieldName = (String) field.get("name");
				fieldCtrl = (String) field.get("ctrl");
				appMapper.addField(tableKey, fieldId, fieldCtrl);
				operatedFieldList.add(field);
			} catch (Exception e) {
				operatedField.put("fieldList", operatedFieldList);
				//出现异常将新增的数据库字段删除
				appMapper.removeFields(operatedField);
				throw new Exception("新增字段名称："+fieldName+"，字段ID："+fieldId+"和原数据存在冲突！");
			}
		}
		return flag;
	}
	/**
	 * 添加明细表字段
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(rollbackFor = Exception.class )
	public boolean addDetails(String tableName, List<Map<String, Object>> addDetails) throws Exception {
		boolean flag = true;
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String, Object>> fieldList = new ArrayList<Map<String,Object>>();
		
		List<Map<String,Object>> operatedFieldList = new ArrayList<Map<String,Object>>();
		for(Map<String,Object> detail:addDetails) {
			fieldList = detail.get("fields")==null?null:(List<Map<String, Object>>)detail.get("fields");
			map.put("tableName", tableName);
			map.put("fieldList", GuavaUtil.turnCtrl(fieldList));
			map.put("detailName", detail.get("id"));
			try {
				appMapper.addDetails(map);
				operatedFieldList.add(detail);
				flag = true;
			} catch (Exception e) {
				//如果出现异常为保证数据不被污染，删除添加的明细表
				removeDetails(tableName, operatedFieldList);
				throw new Exception("新增明细表失败！");
			}
		}
		return flag;
	}
	/**
	 * 更新明细表字段
	 * @throws Exception 
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = Exception.class )
	public boolean updateDetails(String tableName, List<Map<String, Object>> updateDetails) throws Exception {
		boolean flag = true;
		for(Map<String,Object> detail :updateDetails) {
			String detailName = tableName+"_"+detail.get("id");
			List<Map<String,Object>> add = detail.get("addFields")==null?null:(List<Map<String,Object>>)detail.get("addFields");
			List<Map<String,Object>> update = detail.get("updateFields")==null?null:(List<Map<String,Object>>)detail.get("updateFields");
			List<Map<String,Object>> remove = detail.get("removeFields")==null?null:(List<Map<String,Object>>)detail.get("removeFields");
			try {
				if(add!=null&&flag) {
					//添加字段
					flag = addFields(detailName, add);
				}
				if(update!=null&&flag) {
					//更新字段
					flag = updateFields(detailName, update);
				}
				if(remove!=null&&flag) {
					//删除字段
					flag = removeFields(detailName, remove);
				}
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
		}
		return flag;
	}
	/**
	 * 删除明细表
	 * @throws Exception 
	 */
	@Override
	@Transactional(rollbackFor = Exception.class )
	public boolean removeDetails(String tableName, List<Map<String, Object>> removeDetails) throws Exception {
		boolean flag = true;
		Map<String,Object> map = new HashMap<String,Object>();
		//map.put("fieldList", MapUtils.turnCtrl(removeDetails));
		for(Map<String,Object> detail:removeDetails) {
			map.put("tableName", tableName+"_"+detail.get("id"));
			try {
				//先判断明细表是否存在，存在则进行删除操作
				if(ifTableExistInDataBase(tableName+"_"+detail.get("id"))){
					appMapper.removeDetails(map);
				}
				flag = true;
			} catch (Exception e) {
				flag = false;
				throw new Exception("删除明细表"+tableName+"_"+detail.get("id")+"失败！");
			}
		}
		return flag;
	}
	
	
	
	/**
	 * 对明细表进行操作
	 * @param tableName
	 * @param dataMap
	 * @return
	 * @throws Exception 
	 */
	@Override
	@Transactional(rollbackFor = Exception.class )
	public boolean operateDetailTable(String tableName,Map<String,Object> dataMap) throws Exception {
		boolean flag= true;
		List<Map<String,Object>> addDetails = dataMap.get("addDetails")==null?null:(List<Map<String, Object>>) dataMap.get("addDetails");
		List<Map<String,Object>> updateDetails = dataMap.get("updateDetails")==null?null:(List<Map<String, Object>>) dataMap.get("updateDetails");
		List<Map<String,Object>> removeDetails = dataMap.get("removeDetails")==null?null:(List<Map<String, Object>>) dataMap.get("removeDetails");
		//对明细表进行操作，增删改
		try {
			//更新明细表
			if(updateDetails!=null&&flag) {
				flag = updateDetails(tableName,updateDetails);
			}
			//删除明细表
			if(removeDetails!=null&&flag) {
				flag = removeDetails(tableName,removeDetails);
			}
			//添加明细表
			if(addDetails!=null&&flag) {
				flag = addDetails(tableName,addDetails);
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return flag;
	}
	/**
	 * 对主表进行操作
	 * @param tableName
	 * @param dataMap
	 * @return
	 * @throws Exception 
	 */
	@Override
	@Transactional(rollbackFor = Exception.class )
	public boolean operateTable(String tableName,Map<String,Object> dataMap) throws Exception {
		boolean flag = true;
		List<Map<String,Object>> addFields = dataMap.get("addFields")==null?null:(List<Map<String, Object>>) dataMap.get("addFields");
		List<Map<String,Object>> updateFields = dataMap.get("updateFields")==null?null:(List<Map<String, Object>>) dataMap.get("updateFields");
		List<Map<String,Object>> removeFields = dataMap.get("removeFields")==null?null:(List<Map<String, Object>>) dataMap.get("removeFields");
		//对主表字段进行操作，增删改
		try {
			if(removeFields!=null&&flag) {
				//删除表字段
				flag = removeFields(tableName, removeFields);
			}
			if(updateFields!=null&&flag) {
				//更新表字段
				flag = updateFields(tableName, updateFields);
			}
			if(addFields!=null&&flag) {
				//添加表字段
				flag = addFields(tableName, addFields);
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return flag;
	}
	/**
	 * 根据sql查询总量
	 */
	@Override
	public int getCount(String sql) {
		return appMapper.getCount(sql);
	}
	/**
	 * 根据sql查询数据信息
	 */
	@Override
	public List<Map<String, Object>> getInfoBySql(String sql) {
		return appMapper.getInfoBySql(sql);
	}
	/**
	 * 翻译表字段信息
	 */
	@Override
	public Map<String, Object> getFormDataByField(Map<String,Object> data) {
		Map<String,Object> result = new HashMap<String,Object>();
		String tableName = (String) data.get("tableName");
		String field = (String) data.get("field");
		Object value = data.get("value");
		String byField = (String) data.get("byField");
		String sql = "select "+field+" from "+tableName+" where "+byField+" = '"+value+"'";
		Map<String,Object> formData =  appMapper.getFormDataByField(sql);
		result.put("value", formData.get(field));
		return result;
	}
	/**
	 * 查询数据
	 */
	@Override
	public List<Map<String, Object>> queryDataBySqlMap(Map<String, Object> map) {
		return appMapper.queryData(map);
	}
	/**
	 * 查询数量
	 */
	@Override
	public int queryCountBySqlMap(Map<String, Object> dataMap) {
		return appMapper.queryCount(dataMap);
	}
	/**
	 * 流水号生成
	 * @return
	 */
	@Override
	public String serialBuilder(Map<String,Object> raw) {
		Date date = new Date();
		Object tableKey = "_app_"+raw.get("tableKey");
		String format = raw.get("format").toString();
		int sequence = 0;
		boolean flagInitial = false;
		try{
		}catch(EmptyResultDataAccessException e){
			flagInitial = true;
		}
		if(!flagInitial){
			sequence = 0;
			Integer lastSequence = appMapper.getCount("select count(1) from "+tableKey);
			Map<String,Object> id = appMapper.getFormDataByField("select id from "+tableKey+" order by id desc limit 1");
			Integer lastId = id==null?0:Integer.parseInt(id.get("id").toString());
			
			if(lastId!=null && lastId>lastSequence){
				lastSequence = lastId;
			}
			System.out.println(lastId);
			if(lastSequence!=null)
				sequence = lastSequence+1;
		}else{
			sequence = 0;
		}
		String flowingId = "";
		int count = 0;
		String [] datePlaceHolder = new String[]{"tC","ty","tm","td","tH","tM","tS"};
		for(String dpStr:datePlaceHolder){
			if(format.contains(dpStr)){
				count++;
			}

		}
		Object [] args = null;
		if(count>0){
			args = new Object[count+1];
			for(int i = 0;i<args.length;i++){
				if(i!=args.length-1){
					args[i] = date;
				} else{
					args[i] = sequence;
				}

			}
		}else{
			args = new Object[1];
			args[0] = sequence;
		}
		flowingId = String.format(format, args);
		return flowingId;
	}
	@Override
	public Long queryRoleIdByUserId(Long userId) {
		return appMapper.queryRoleIdByUserId(userId);
	}
	@Override
	public boolean saveRoleAndMod(Long modId, Long roleId) {
		boolean flag = true;
		try {
			appMapper.saveRoleAndMod(modId, roleId);
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
	
	@Override
	public void deleteForm(Map<String, Object> map) {
		appMapper.deleteForm(map);
	}
	
	@SuppressWarnings("unused")
	@Override
	public Long NamedCUDHoldId(String tableName, Map<String, Object> paramMap) {
		String sql = "insert into "+tableName+"(" ;
		Set<String> keySet = paramMap.keySet();
		for(String key :keySet) {
			sql += key+",";
		}
		return appMapper.NamedCUDHoldId(tableName, paramMap);
	}
	@Override
	public void NamedCUD(String tableName, Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		appMapper.NamedCUD(tableName, paramMap);
	}
	@Override
	public int CUD(String sql) {
		// TODO Auto-generated method stub
		try {
			appMapper.CUD(sql);	
			return 1;
		} catch (Exception e) {
			return 0;
		}
	}
	@Override
	public Object getObject(String sql,String key) {
		Map<String,Object> map = appMapper.getObject(sql);
		return map.get(key);
	}
	@Override
	public List<Map<String, Object>> getList(String sql) {
		try {
			return appMapper.getList(sql);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return new ArrayList<>();
		}
	}
	@Override
	public Map<String, Object> getMap(String sql) {
		return appMapper.getMap(sql);
	}
	@Override
	public List<Object> getObjectList(String sql) {
		return appMapper.getObjectList(sql);
	}
	/**
	 * sliu
	 * 判断表是否已存在
	 */
	@Override
	public boolean ifTableExistInDataBase(String tableName) {
		String dataBaseName = appMapper.getDataBaseName();
		return appMapper.ifTableExistInDataBase(dataBaseName, tableName)>0?true:false;
	}
	
	@Override
	public boolean ifColumnExistInTable(String table, String column) {
		String dataBase = appMapper.getDataBaseName();
		return appMapper.ifColumnExistInTable(dataBase, table, column)>0?true:false;
	}
	/**
	 * sliu
	 * 如果表名存在重复，加数字后缀名
	 */
	@Override
	public String updateTableKey(String tableKey) {
		String updateTableKey = tableKey;
		boolean ifExist = this.ifTableExistInDataBase("_app_"+tableKey);
		int  i = 1;
		while(ifExist) {
			updateTableKey = tableKey+i;
			ifExist = this.ifTableExistInDataBase("_app_"+updateTableKey);
			i++;
			if(i==100) {
				break;
			}
		}
		return updateTableKey;
	}
	@Override
	public Map<String, Object> getFormDataByFieldList(List<String> fields, String tableName, Long formId) {
		return appMapper.getFormDataByFieldList(fields, tableName, formId);
	}
	@Override
	public List<Map<String, Object>> getFormDataListByFieldList(List<String> fields, String tableName, Long formId) {
		return appMapper.getFormDataListByFieldList(fields, tableName, formId);
	}
	
}
