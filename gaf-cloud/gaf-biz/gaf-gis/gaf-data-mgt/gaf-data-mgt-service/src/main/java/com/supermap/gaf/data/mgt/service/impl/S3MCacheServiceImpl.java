/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.data.mgt.service.impl;

import com.supermap.data.*;
import com.supermap.data.processing.OSGBCacheBuilder;
import com.supermap.data.processing.StorageType;
import com.supermap.gaf.commontypes.MessageResult;
import com.supermap.gaf.data.mgt.entity.DataSourceInfo;
import com.supermap.gaf.data.mgt.service.S3MCacheService;
import com.supermap.gaf.data.mgt.support.DatasourceParser;
import com.supermap.gaf.data.mgt.util.FileUtils;
import com.supermap.gaf.exception.GafException;
import com.supermap.gaf.utils.LogUtil;
import com.supermap.realspace.CacheFileType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author:yd
 * @date:2021/3/25
 * @Date 2021-3-01
 */
@Service
public class S3MCacheServiceImpl implements S3MCacheService {

    private static final Logger logger = LogUtil.getLocLogger(S3MCacheServiceImpl.class);

    private static final int LOD_SIZE = 4;

    @Value("${supermap.gaf.s3m-threads-num:4}")
    private int threadsNum;

    @Autowired
    private DatasourceParser datasourceParser;

    @Override
    public MessageResult<String> buildS3M(DataSourceInfo dataSourceInfo, List<String> datasetNames, boolean isOverWrite, String outputFolder) {
        try {
            if (null == dataSourceInfo || CollectionUtils.isEmpty(datasetNames)) {
                throw new GafException("参数不合法");
            }
            if (StringUtils.isEmpty(dataSourceInfo.getDsName())) {
                throw new GafException("数据源名称不能为空");
            }
            // 数据集名称参数去重
            datasetNames = datasetNames.stream().distinct().collect(Collectors.toList());

            Datasource datasource = datasourceParser.parserDatasource(dataSourceInfo);

            // 切片结果存放路径
            if (StringUtils.isEmpty(outputFolder)) {
                outputFolder = String.format("%s%s", dataSourceInfo.getDsName(), File.separator);
            } else {
                outputFolder = String.format("%s%s", outputFolder, File.separator);
            }
            final String resultOutputFolder = outputFolder;
            File resultFolder = new File(resultOutputFolder);
            boolean mkdirResult = false;
            if (!resultFolder.exists()) {
                // 创建新的缓存目录
                mkdirResult = resultFolder.mkdirs();
                if (!mkdirResult) {
                    throw new GafException("创建缓存结果存放文件目录失败");
                }
            }
            // 不是新创建的，且需要覆盖，则先删除再创建目录
            if (isOverWrite && !mkdirResult) {
                // 删除历史缓存文件
                boolean deleteResult = FileUtils.deleteFile(resultFolder);
                if (!deleteResult) {
                    throw new GafException("删除历史缓存失败");
                }
                // 创建新的缓存目录
                resultFolder.mkdirs();
            }

            // 已生成缓存的数据集名称列表
            final List<String> subFileNames = FileUtils.listFileName(resultFolder);
            final Datasets datasets = datasource.getDatasets();
            datasetNames.stream().forEach(datasetName -> {
                Dataset dataset = datasets.get(datasetName);
                if (null == dataset) {
                    return;
                }
                DatasetType datasetType = dataset.getType();
                if (!DatasetType.MODEL.equals(datasetType)) {
                    logger.error(String.format("数据集【%s】类型不为模型数据集", datasetName));
                    return;
                }
                if (subFileNames.contains(datasetName) && !isOverWrite) {
                    return;
                }
                buildOneDatasetS3M(dataset, resultOutputFolder);
            });
            return MessageResult.successe(String.class).data(resultOutputFolder).build();
        } catch (Exception e) {
            logger.error("生成s3m缓存异常", e);
            return MessageResult.failed(String.class).message(e.getMessage()).build();
        }
    }

    /**
     * 生成一个数据集的s3m缓存
     *
     * @param dataset
     * @param resultOutputFolder
     */
    private void buildOneDatasetS3M(Dataset dataset, String resultOutputFolder) {
        try {
            long startTime = System.currentTimeMillis();
            String cacheName = dataset.getName();
            DatasetVector datasetVector = (DatasetVector) dataset;

            OSGBCacheBuilder cacheBuilder = new OSGBCacheBuilder(datasetVector, resultOutputFolder, cacheName);
            cacheBuilder.setFileType(CacheFileType.S3M);
            cacheBuilder.setStorageType(StorageType.Compact);
            cacheBuilder.setLODSize(LOD_SIZE);
            cacheBuilder.setTextureSharing(true);
            //设置生成缓存线程数
            cacheBuilder.setProcessThreadsCount(threadsNum);
            SteppedListener steppedListener = steppedEvent -> {
                logger.info(String.format("完成百分比： %d%%", steppedEvent.getPercent()));
            };
            cacheBuilder.addSteppedListener(steppedListener);
            boolean isBuild = cacheBuilder.build();
            if (isBuild) {
                long endTime = System.currentTimeMillis();
                long importTime = endTime - startTime;
                logger.info(String.format("缓存【%s】创建成功! 用时:【%d】毫秒", cacheName, importTime));
            } else {
                logger.info("缓存创建失败");
            }
            cacheBuilder.removeSteppedListener(steppedListener);
            cacheBuilder.dispose();
        } catch (Exception e) {
            logger.error(String.format("数据集【%s】生成s3m缓存异常", dataset.getName()), e);
        }
    }
}
