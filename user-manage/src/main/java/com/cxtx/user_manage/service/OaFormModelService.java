package com.cxtx.user_manage.service;

import com.cxtx.user_manage.domain.OaProcessHis;
import com.github.pagehelper.PageInfo;
import com.cxtx.user_manage.domain.OaFormModel;
import java.util.List;
import java.util.Map;


public interface OaFormModelService{

    int deleteByPrimaryKey(Long id);

    int insertSelective(OaFormModel record);

    OaFormModel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OaFormModel record);

    PageInfo<OaFormModel> queryByPage(Map params);

    List<OaFormModel> selectAll(Map params);

    /**
     * 根据表单模型ID查询发起节点的表单配置
     * @param formModId
     * @return
     */
    Map<String, Object> getStartElementConfigByFormModId(Long formModId);


    List<Map<String,Object>> getAllMods(String companyId, String roleId);
    List<Map<String,Object>> getAllCats(String companyId, String roleId);
    List<Map<String,Object>> getModsGroupByCatId(String companyId, String roleId);
    /**
     *
     * @param map
     * tableKey
     * columns: list
     * values: list
     * @return
     */
    int insertForm(Map<String, Object> map);

    /**
     *
     * @param map
     * tableKey
     * id
     * @return
     */
    void deleteForm(Map<String, Object> map);

    /**
     *
     * @param map
     * tableKey
     * sql
     * id
     * @return
     */
    void updateForm(Map<String, Object> map);

    /**
     * @param map tableKey
     *            sql
     *            did
     * @return
     */
    void updateDetailForm(Map<String, Object> map);

    /**
     *
     * @param map
     * tableKey
     * id
     * @return
     */
    Map<String, Object> queryForm(Map<String, Object> map);
    /**
     *
     * @param map
     * tableKey
     * sql
     * @return
     */
    List<Map<String,Object>> getListForm(Map<String, Object> map);
    /**
     *
     * @param map,page
     * tableKey
     * sql
     * @return
     */
    @SuppressWarnings("rawtypes")
    IPage getPageForm(Page page, Map<String, Object> map);

    OaFormMod getFormModByFlowModId(Long flowModId);
    /**
     * 插入表单数据并返回表单ID
     * @param data
     * @param user
     * @return
     * @throws Exception
     */
    Long insertFormData(Map<String,Object> data,SysUserLoginVO user)throws Exception;

    int updateFormData(Map<String,Object> data,SysUserLoginVO user)throws Exception;

    Result<?> deleteFormMod(Long id) throws Exception;

    public Map<String, Object> getAllModsLists(Long curUserId) throws BaseException;

    public List<Map<String, Object>> getListForms(Long curUserId) throws BaseException ;

    public Result<?> updates( OaFormMod oaFormMod,Long curUserId) throws Exception;
    /**
     * 根据主表、明细表和主表ID查询表单信息
     * @param tableKey
     * @param detailKeys
     * @param formId
     * @return
     */
    Map<String,Object> getFormData(String tableKey,String detailKeys,Long formId);
    /**
     * 根据表单模型ID查询发起节点的表单配置
     * @param formModId
     * @return
     */
    Map<String, Object> getStartElemntConfigByFormModId(Long formModId);

}