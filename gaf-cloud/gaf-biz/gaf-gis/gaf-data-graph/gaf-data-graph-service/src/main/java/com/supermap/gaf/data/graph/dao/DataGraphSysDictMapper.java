package com.supermap.gaf.data.graph.dao;

import com.supermap.gaf.sys.mgt.commontype.SysDict;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author : duke
 * @since 2021/5/6 8:03 PM
 */
@Component
public interface DataGraphSysDictMapper {
    /**
     * 通过extProperties扩展字段模糊查询查询字典
     * @param dictCode
     * @param extProperties
     * @return
     */
    List<SysDict> listSysDictViaExtPropertiesLike(@Param("dictCode") String dictCode, @Param("extProperties")String extProperties);

    /**
     * 通过dictDesc字典描述查询字典
     * @param dictCode
     * @param dictDesc
     * @return
     */
    List<SysDict> listSysDictByDesc(@Param("dictCode") String dictCode, @Param("dictDesc")String dictDesc);
}
