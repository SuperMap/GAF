package com.supermap.gaf.storage.service;

import com.supermap.gaf.storage.entity.ConfigName;
import com.supermap.gaf.storage.entity.Space;
import com.supermap.gaf.storage.entity.SpaceConfig;
import com.supermap.gaf.storage.entity.vo.SpaceConfigSelectVo;
import com.supermap.gaf.storage.entity.vo.SpaceSelectVo;
import com.supermap.gaf.storage.utils.Page;

import java.util.Collection;
import java.util.List;

/**
 * 服务类
 *
 * @author zrc
 * @date yyyy-mm-dd
 */
public interface TenantSpaceConfigService {

    /**
     * 根据id查询
     *
     * @return
     */
    SpaceConfig getById(String id);

    /**
     * 分页条件查询
     *
     * @param pageNum  当前页码
     * @param pageSize 每页数量
     * @return 分页对象
     */
    Page<SpaceConfig> listByPageCondition(SpaceConfigSelectVo spaceConfigSelectVo, int pageNum, int pageSize);


    /**
     * 新增
     *
     * @return 新增的globalServerConfig
     */
    void insertGlobalServerConfig(SpaceConfig spaceConfig);

    /**
     * 删除
     */
    void deleteGlobalServerConfig(String id);

    /**
     * 批量删除
     **/
    void batchDelete(List<String> ids);

    /**
     * 更新
     *
     * @return 更新后的globalServerConfig
     */
    void updateGlobalServerConfig(SpaceConfig spaceConfig);

    Page<Space> allocated(SpaceSelectVo spaceSelectVo, Integer pageNum, Integer pageSize);

    Collection<ConfigName> getAllNames(String ower);
}
