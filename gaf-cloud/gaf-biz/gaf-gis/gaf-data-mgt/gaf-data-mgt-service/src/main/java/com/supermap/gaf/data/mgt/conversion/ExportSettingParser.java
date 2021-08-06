/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.conversion;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.supermap.data.conversion.ExportSetting;

import java.util.function.BiConsumer;

/**
 * 导出设置解析器
 *
 * @author wxl
 * @since 2021-8-3
 */
public interface ExportSettingParser {

    /**
     * 将JSONObject对象里的导出设置信息解析为ExportSetting类对象,用于后续iobjects导出
     * @param exportSettingJO 导出设置信息
     * @return ExportSetting类对象
     */
    ExportSetting parseExportSetting(JSONObject exportSettingJO);

    /**
     * 解析辅助类
     * 提供通用的导出设置信息的解析
     * 不同导出类型的个别的导出设置信息 通过回调函数解析
     *
     * @param exportSettingJO 导出设置信息 JSONObject对象
     * @param clazz iobjects中类ExportSetting的子类
     * @param setExport  回调函数 参数是ExportSetting的子类对象 和 导出设置信息基本部分(basePart)JSONObject对象
     * @param <S> ExportSetting的子类
     * @return 解析后得到的ExportSetting的子类对象
     */
    default <S extends ExportSetting>  S parse(JSONObject exportSettingJO, Class<S> clazz, BiConsumer<S,JSONObject> setExport) {
        JSONObject basePartJO = exportSettingJO.getJSONObject("basePart");
        ParserConfig parserConfig = ConversionConfig.getParseConfig();
        S exportSetting = JSON.parseObject(basePartJO.toJSONString(), clazz, parserConfig);
        setExport.accept(exportSetting,basePartJO);
        return exportSetting;
    }

}
