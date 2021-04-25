/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.authority.dao;

import com.supermap.gaf.authority.commontype.AuthResourceMenu;
import com.supermap.gaf.authority.vo.AuthResourceMenuSelectVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * 菜单mapper
 * @date:2021/3/25
 * @author wxl
 */
@Mapper
@Component
public interface AuthResourceMenuMapper{
    /**
     * 根据id查询菜单
     * @param resourceMenuId 菜单id
     * @return 菜单 未查询到则返回null
     */
    AuthResourceMenu select(@Param("resourceMenuId") String resourceMenuId);

    /**
     * 根据主键 resourceMenuId 查询 能查到已被逻辑删除的
     * @param resourceMenuId 菜单id
     * @return 菜单 未查询到则返回null
     */
    AuthResourceMenu selectReal(@Param("resourceMenuId") String resourceMenuId);

    /**
     * 查询所有
     * @return 菜单集合
     */
    List<AuthResourceMenu> listMenus();

    /**
     * 分页查询菜单
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 菜单集合
     */
    List<AuthResourceMenu> pageList(AuthResourceMenuSelectVo selectVo);
	
    /**
     * 分页等值查询菜单
     * @param selectVo 分页参数 如偏移量，每页条数, 等值查询字段名 字段值 排序字段名 排序方式
     * @return 菜单集合
     */
    List<AuthResourceMenu> searchList(AuthResourceMenuSelectVo selectVo);

    /**
     * 分页查询
     * 针对偏移量过大的分页查询sql优化
     * @param selectVo 分页参数 如偏移量，每页条数
     * @return 菜单集合
     */
    List<AuthResourceMenu> bigOffsetPageList(AuthResourceMenuSelectVo selectVo);

    /**
     * 查询菜单总数
     * @return 数量
     */
    int pageListCount();
    /**
     * 统计单字段模糊匹配条件查询后的结果数量
     * @param fieldName 字段名
     * @param fieldValue 字段值
     * @return 数量
     */
	Integer countOneField(@Param("fieldName") String fieldName, @Param("fieldValue") Object fieldValue);
    /**
     * 新增菜单
     * @param authResourceMenu 菜单
     * @return 新增的数量
     */
    int insert(AuthResourceMenu authResourceMenu);
    /**
     * 批量新增菜单
     * @param authResourceMenus  菜单集合
     * @return 新增的数量
     */
    int batchInsert(List<AuthResourceMenu> authResourceMenus);
    /**
     * 根据id集合批量删除菜单
     * @param resourceMenuIds 菜单id集合
     * @return 删除的数量
     */
    int batchDelete(List<String> resourceMenuIds);
    /**
     * 根据菜单id删除菜单
     * @param resourceMenuId 菜单id
     * @return 影响的行数即删除的数量
     */
    int delete(@Param("resourceMenuId") String resourceMenuId);
    /**
     * 更新菜单
     * @param authResourceMenu   菜单
     * @return 影响的行数即更新的数量
     */
    int update(AuthResourceMenu authResourceMenu);

    /**
     * 统计条件查询的结果数量
     * @param queryMenu 查询条件 注意： 该对象的属性值至少有一个不能为null
     * @return 若未查询到则返回0
     */
    Integer countByCombination(AuthResourceMenu queryMenu);

    /**
     * 通过id集合查询菜单
     * @param menuIdList 菜单id集合
     * @return 若未查询到则返回为空集合
     */
    List<AuthResourceMenu> selectByIds(@Param("menuIdList") List<String> menuIdList);

    /**
     * 通过模块id集合查询菜单
     * @param moduleIdList 模块id集合
     * @return 若未查询到则返回为空集合
     */
    List<AuthResourceMenu> selectByModuleIds(@Param("moduleIdList") List<String> moduleIdList);
}
