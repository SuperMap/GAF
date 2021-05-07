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
    List<SysDict> listSysDictViaExtPropertiesLike(@Param("dictCode") String dictCode, @Param("extProperties")String extProperties);
}
