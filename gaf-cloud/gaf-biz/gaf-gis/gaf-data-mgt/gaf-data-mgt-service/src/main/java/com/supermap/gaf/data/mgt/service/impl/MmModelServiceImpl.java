package com.supermap.gaf.data.mgt.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.supermap.gaf.commontypes.tree.DefaultTreeNode;
import com.supermap.gaf.data.mgt.entity.MmModel;
import com.supermap.gaf.data.mgt.entity.MmTable;
import com.supermap.gaf.data.mgt.mapper.MmModelMapper;
import com.supermap.gaf.data.mgt.mapper.MmTableMapper;
import com.supermap.gaf.data.mgt.service.MmModelService;
import com.supermap.gaf.data.mgt.util.Page;
import com.supermap.gaf.data.mgt.vo.MmModelSelectVo;
import com.supermap.gaf.data.mgt.vo.MmTableSelectVo;
import com.supermap.gaf.shiro.SecurityUtilsExt;
import com.supermap.gaf.shiro.commontypes.ShiroUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 模型服务实现类
 * @author wxl 
 * @date yyyy-mm-dd
 */
@Service
public class MmModelServiceImpl implements MmModelService {
    
	private static final Logger  log = LoggerFactory.getLogger(MmModelServiceImpl.class);
	
	@Autowired
    private MmModelMapper mmModelMapper;

	@Autowired
	private MmTableMapper mmTableMapper;
	
	@Override
    public MmModel getById(String modelId){
        if(modelId == null){
            throw new IllegalArgumentException("modelId不能为空");
        }
        return  mmModelMapper.select(modelId);
    }
	
	@Override
    public Page<MmModel> listByPageCondition(MmModelSelectVo mmModelSelectVo, int pageNum, int pageSize) {
        PageInfo<MmModel> pageInfo = PageHelper.startPage(pageNum, pageSize).doSelectPageInfo(() -> {
            mmModelMapper.selectList(mmModelSelectVo);
        });
        return Page.create(pageInfo.getPageNum(),pageInfo.getPageSize(),(int)pageInfo.getTotal(),pageInfo.getPages(),pageInfo.getList());
    }

	@Override
    public MmModel insertMmModel(MmModel mmModel){
        // 主键非GeneratedKey，此处添加自定义主键生成策略
		mmModel.setModelId(UUID.randomUUID().toString());
		
		ShiroUser shiroUser = SecurityUtilsExt.getUser();
		mmModel.setCreatedBy(shiroUser.getAuthUser().getName());
		mmModel.setUpdatedBy(shiroUser.getAuthUser().getName());
        mmModelMapper.insert(mmModel);
        return mmModelMapper.select(mmModel.getModelId());
    }
	
	@Override
    public void batchInsert(List<MmModel> mmModels){
		if (mmModels != null && mmModels.size() > 0) {
	        mmModels.forEach(mmModel -> {
				mmModel.setModelId(UUID.randomUUID().toString());
				ShiroUser shiroUser = SecurityUtilsExt.getUser();
				mmModel.setCreatedBy(shiroUser.getAuthUser().getName());
				mmModel.setUpdatedBy(shiroUser.getAuthUser().getName());
            });
            mmModelMapper.batchInsert(mmModels);
        }
        
    }
	
	@Override
    public MmModel deleteMmModel(String modelId){
        MmModel mmModel = mmModelMapper.select(modelId);
        mmModelMapper.delete(modelId);
        return mmModel;
    }

	@Override
    public void batchDelete(List<String> modelIds){
        mmModelMapper.batchDelete(modelIds);
    }
	
	@Override
    public MmModel updateMmModel(MmModel mmModel){
		ShiroUser shiroUser = SecurityUtilsExt.getUser();
		mmModel.setUpdatedBy(shiroUser.getAuthUser().getName());
		mmModelMapper.update(mmModel);
        return mmModelMapper.select(mmModel.getModelId());
    }

    @Override
    public List<DefaultTreeNode> modelTablesTree() {
        MmModelSelectVo mmModelSelectVo = new MmModelSelectVo();
        List<MmModel> mmModels = mmModelMapper.selectList(mmModelSelectVo);
        // todo: 之后再优化为直接连表查询
        Map<String,String> map = new HashMap<>();
        map.put("type","model");
        Map<String,String> map2 = new HashMap<>();
        map2.put("type","table");
        return mmModels.stream().map(model -> {
            DefaultTreeNode treeNode = new DefaultTreeNode();
            treeNode.setKey(model.getModelId());
            treeNode.setParentId("0");
            treeNode.setSortSn(model.getSortSn());
            treeNode.setTitle(model.getModelName());
            treeNode.setUserObject(map);

            MmTableSelectVo mmTableSelectVo = new MmTableSelectVo();
            mmTableSelectVo.setModelId(model.getModelId());
            List<MmTable> mmTables = mmTableMapper.selectList(mmTableSelectVo);
            List<DefaultTreeNode> tables = mmTables.stream().map(mmTable -> {
                DefaultTreeNode node = new DefaultTreeNode();
                node.setKey(mmTable.getTableId());
                node.setParentId(mmTable.getModelId());
                node.setSortSn(mmTable.getSortSn());
                node.setTitle(mmTable.getTableName());
                node.setChildren(null);
                node.setLeaf(true);
                node.setUserObject(map2);
                return node;
            }).collect(Collectors.toList());
            treeNode.setChildren(tables.size() > 0 ? tables:null);
            treeNode.setLeaf(tables.size() <= 0);
            return treeNode;
        }).collect(Collectors.toList());
    }

}
