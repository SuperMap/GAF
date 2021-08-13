package com.supermap.gaf.data.mgt.service;

import com.supermap.gaf.commontypes.tree.DefaultTreeNode;
import com.supermap.gaf.data.mgt.entity.MmModel;
import com.supermap.gaf.data.mgt.util.Page;
import com.supermap.gaf.data.mgt.vo.MmModelSelectVo;

import java.util.List;

/**
 * 模型服务类
 * @author wxl 
 * @date yyyy-mm-dd
 */
public interface MmModelService {
	
	/**
    * id查询模型
    * @return
    */
    MmModel getById(String modelId);
	
	/**
     * 分页条件查询
     * @param mmModelSelectVo 查询条件
     * @param pageNum 当前页数
     * @param pageSize 页面大小
     * @return 分页对象
     */
	Page<MmModel> listByPageCondition(MmModelSelectVo mmModelSelectVo, int pageNum, int pageSize);
	
	
    /**
     * 新增模型
     * @return 
     */
    MmModel insertMmModel(MmModel mmModel);
	
	/**
     * 批量插入
     *
	 */
    void batchInsert(List<MmModel> mmModels);

    /**
     * 删除模型
     * 
     */
    MmModel deleteMmModel(String modelId);

    /**
     * 批量删除
     * 
	 */
    void batchDelete(List<String> modelIds);

    /**
     * 更新模型
     * @return 
     */
    MmModel updateMmModel(MmModel mmModel);

    /**
     * 查询所有模型及其所有逻辑表的构成的树
     * @return
     */
    List<DefaultTreeNode> modelTablesTree();
}
