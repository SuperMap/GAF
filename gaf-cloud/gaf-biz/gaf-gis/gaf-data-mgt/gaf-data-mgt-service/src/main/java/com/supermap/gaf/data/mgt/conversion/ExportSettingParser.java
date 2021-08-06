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


public interface ExportSettingParser {

    ExportSetting parseExportSetting(JSONObject exportSettingJO);

    default <S extends ExportSetting>  S parse(JSONObject exportSettingJO, Class<S> clazz, BiConsumer<S,JSONObject> setExport) {
        JSONObject basePartJO = exportSettingJO.getJSONObject("basePart");
        ParserConfig parserConfig = ConversionConfig.getParseConfig();
        S exportSetting = JSON.parseObject(basePartJO.toJSONString(), clazz, parserConfig);
        setExport.accept(exportSetting,basePartJO);
        return exportSetting;
    }

}
