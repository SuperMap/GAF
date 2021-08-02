/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.data.mgt.mapper;

import com.supermap.gaf.data.mgt.commontype.SysResourceDatasource;
import com.supermap.gaf.data.mgt.vo.SysResourceDatasourceSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 数据源mapper
 * @date:2021/3/25
 * @author wangxiaolong
 */
@Mapper
@Component
public interface SysResourceDatasourceMapper {
    /**
     * 根据数据源id查询数据源
     * @param datasourceId 数据源id
     * @return 数据源 未查询到则返回null
     */
    SysResourceDatasource select(@Param("datasourceId") String datasourceId);

    /**
     * 等值条件和单字段模糊查询
     * @param sysResourceDatasourceSelectVo 查询条件 , 如开始时间 结束时间 是否是空间数据源 数据源分类 数据源类型集合 模糊查询字段名 字段值 排序字段名 排序方式
     * @return 若未查询到则返回空集合
     */
    List<SysResourceDatasource> selectList(SysResourceDatasourceSelectVo sysResourceDatasourceSelectVo);

    /**
     * 新增数据源
     * @param sysResourceDatasource 数据源
     * @return 新增的数量
     */
    int insert(SysResourceDatasource sysResourceDatasource);
    /**
     * 批量新增数据源
     * @param sysResourceDatasources  数据源集合
     * @return 新增的数量
     */
    int batchInsert(List<SysResourceDatasource> sysResourceDatasources);
    /**
     * 根据数据源id集合删除数据源
     * @param datasourceIds 数据源id集合
     * @return 影响的行数即删除的数量
     */
    int batchDelete(List<String> datasourceIds);
    /**
     * 根据数据源id删除数据源
     * @param datasourceId 数据源id
     * @return 影响的行数即删除的数量
     */
    int delete(@Param("datasourceId") String datasourceId);
    /**
     * 更新数据源
     * @param sysResourceDatasource   数据源
     * @return 影响的行数即更新的数量
     */
    int update(SysResourceDatasource sysResourceDatasource);

    /**
     * 查询数据源别名是否重复
     * @param dsName
     * @param isSdx
     * @return
     */
    List<SysResourceDatasource> getByName(@Param("dsName")String dsName, @Param("isSdx")Boolean isSdx,@Param("isTemplate") Boolean isTemplate);
}
