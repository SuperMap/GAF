package com.supermap.gaf.data.mgt.service;

import com.supermap.gaf.data.mgt.entity.MmField;
import com.supermap.gaf.data.mgt.model.FieldTypeInfo;
import com.supermap.gaf.data.mgt.util.Page;
import com.supermap.gaf.data.mgt.vo.MmFieldSelectVo;

import java.util.List;

/**
 * 字段服务类
 * @author wxl 
 * @date yyyy-mm-dd
 */
public interface MmFieldService {
	
	/**
    * id查询字段
    * @return
    */
    MmField getById(String fieldId);

	/**
     * 分页条件查询
     * @param mmFieldSelectVo 查询条件
     * @param pageNum 当前页数
     * @param pageSize 页面大小
     * @return 分页对象
     */
	Page<MmField> listByPageCondition(MmFieldSelectVo mmFieldSelectVo, int pageNum, int pageSize);

    /**
     * 条件查询列表
     * @param mmFieldSelectVo
     * @return
     */
	List<MmField> selectList(MmFieldSelectVo mmFieldSelectVo);

    /**
     * 新增字段
     * @return 
     */
    MmField insertMmField(MmField mmField);
	
	/**
     * 批量插入
     *
	 */
    void batchInsert(List<MmField> mmFields);

    /**
     * 删除字段
     * 
     */
    void deleteMmField(String fieldId);

    /**
     * 批量删除
     * 
	 */
    void batchDelete(List<String> fieldIds);

    /**
     * 更新字段
     * @return 
     */
    MmField updateMmField(MmField mmField);

    /**
     * 根据模型类型查询对应的字段的数据类型
     * @param modelType 模型类型
     * @return 字段的数据类型映射
     */
    List<FieldTypeInfo> listTypeInfos(String modelType);
}
