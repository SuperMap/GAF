/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.configmgt.util;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * mybatis批量插入更新工具类
 *
 * @author wxl
 * @date:2021/3/25
 */
@Component
public class MybatisBatchUtil {
    /**
     * 批量执行模式下每次flushStatements()的默认值
     */
    public static final int DEFAULT_BATCH_SIZE = 1000;

    private static SqlSessionFactory sqlSessionFactory;


    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        MybatisBatchUtil.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * 批量插入或更新方法
     * 注意1：非常重要
     * 调用者必须在调用该方法后提交事务，例如在调用该方法的方法上添加@Transactional或者使用aspectJ的声明式事务
     * 原因是：该工具类的批量插入或更新方法开启事务手动提交 但是在方法结束并未手动提交事务，调用者必须提交事务
     * 注意2：需要在Spring容器初始化完成之后才可以使用静态方法
     *
     * @param mapperClass mybatis的mapper类对象 应该使用xxxMapper.class
     * @param entityList  实体类集合
     * @param func        回调方法
     * @param <M>         Mapper类
     * @param <T>         实体类
     * @return 若批量执行成功则返回true 否则会抛出运行时异常
     */
    public static <M, T> boolean insertOrUpdateBatch(Class<M> mapperClass, List<T> entityList, BiConsumer<M, T> func) {
        return insertOrUpdateBatch(mapperClass, entityList, DEFAULT_BATCH_SIZE, func);
    }

    /**
     * 批量插入或更新方法
     * 注意1：非常重要
     * 调用者必须在调用该方法后提交事务，例如在调用该方法的方法上添加@Transactional或者使用aspectJ的声明式事务
     * 原因是：该工具类的批量插入或更新方法开启事务手动提交 但是在方法结束并未手动提交事务，调用者必须提交事务
     * 注意2：需要在Spring容器初始化完成之后才可以使用静态方法
     *
     * @param mapperClass mybatis的mapper类对象
     * @param entityList  实体类集合
     * @param batchSize   每次的数量
     * @param func        回调方法
     * @param <M>         Mapper类
     * @param <T>         实体类
     * @return 若批量执行成功则返回true 否则会抛出运行时异常
     */
    public static <M, T> boolean insertOrUpdateBatch(Class<M> mapperClass, List<T> entityList, int batchSize, BiConsumer<M, T> func) {
        if (entityList == null || entityList.size() == 0) {
            throw new MybatisBatchException("entityList must not be empty");
        }
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            M modelMapper = sqlSession.getMapper(mapperClass);
            int i = 0;
            for (T entity : entityList) {
                func.accept(modelMapper, entity);
                if (i >= 1 && i % batchSize == 0) {
                    sqlSession.flushStatements();
                }
                i++;
            }
            sqlSession.flushStatements();
        }
        return true;
    }

    /**
     * 批量插入或更新方法
     * 该方法使用Spring的声明式事务注解需要开启事务管理
     *
     * @param mapperClass mybatis的mapper类对象
     * @param entityList  实体类集合
     * @param func        回调方法
     * @param <M>         Mapper类
     * @param <T>         实体类
     * @return 若批量执行成功则返回true 否则会抛出运行时异常
     */
    @Transactional(rollbackFor = Exception.class)
    public <M, T> boolean saveOrUpdateBatch(Class<M> mapperClass, List<T> entityList, BiConsumer<M, T> func) {
        return saveOrUpdateBatch(mapperClass, entityList, DEFAULT_BATCH_SIZE, func);
    }

    /**
     * 批量插入或更新方法
     * 该方法使用Spring的声明式事务注解需要开启事务管理
     *
     * @param mapperClass mybatis的mapper类对象
     * @param entityList  实体类集合
     * @param batchSize   每次的数量
     * @param func        回调方法
     * @param <M>         Mapper类
     * @param <T>         实体类
     * @return 若批量执行成功则返回true 否则会抛出运行时异常
     */
    @Transactional(rollbackFor = Exception.class)
    public <M, T> boolean saveOrUpdateBatch(Class<M> mapperClass, List<T> entityList, int batchSize, BiConsumer<M, T> func) {
        return insertOrUpdateBatch(mapperClass, entityList, batchSize, func);
    }

}

class MybatisBatchException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MybatisBatchException(String message) {
        super(message);
    }

    public MybatisBatchException(Throwable throwable) {
        super(throwable);
    }

    public MybatisBatchException(String message, Throwable throwable) {
        super(message, throwable);
    }
}


