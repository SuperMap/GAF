package com.supermap.gaf.data.graph.service;

/**
 * @author : duke
 * @since 2021/5/6 5:27 PM
 */
public interface DataGraphImportService {
    /**
     * 行政区划字典数据导入图谱
     */
    void importXzqh();

    /**
     * 自然资源数据体系字典数据导入图谱
     */
    void importZrzysjtx();
}
