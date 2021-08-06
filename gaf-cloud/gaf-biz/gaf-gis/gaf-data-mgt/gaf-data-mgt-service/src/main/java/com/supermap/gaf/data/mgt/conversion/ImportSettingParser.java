/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.conversion;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.supermap.data.FieldInfo;
import com.supermap.data.FieldInfos;
import com.supermap.data.conversion.ImportDataInfo;
import com.supermap.data.conversion.ImportDataInfos;
import com.supermap.data.conversion.ImportSetting;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface ImportSettingParser{


    ImportSetting parseImportSetting(JSONObject importSettingJO, Consumer<ImportSetting> consumer);

    //ImportDataInfo parseImportDataInfo(String jsonStr, ImportDataInfo importDataInfo);

    //default  void disposeImportDataInfos(ImportDataInfos importDataInfos) {
    //}
    //
    //default  void disposeImportSetting(ImportSetting importSetting) {
    //    importSetting.dispose();
    //}

    //default <D extends ImportDataInfo> void dispose(Class<D> dataInfoClazz,D importDataInfo) {
    //}

    //default <D extends ImportDataInfo> void a (D importDataInfo, JSONObject jsonObject) {
    //    JSONObject jo = jsonObject.getJSONObject("changeFieldName");
    //    String oldFieldName = jo.getString("oldFieldName");
    //    String newFeildName = jo.getString("newFeildName");
    //    importDataInfo.changeFieldName(oldFieldName,newFeildName);
    //}

    default void parseTargetFieldInfos(JSONObject jsonObject, Consumer<FieldInfos> consumer) {
        JSONArray targetFieldInfos = jsonObject.getJSONArray("targetFieldInfos");
        if (null != targetFieldInfos && !targetFieldInfos.isEmpty()) {
            FieldInfos fieldInfos = new FieldInfos();
            for (int i = 0; i < targetFieldInfos.size(); i++) {
                JSONObject fieldInfoJO = targetFieldInfos.getJSONObject(i);
                String s = fieldInfoJO.toJSONString();
                FieldInfo fieldInfo = JSON.parseObject(s, FieldInfo.class, ConversionConfig.getParseConfig());
                fieldInfos.add(fieldInfo);
            }
            consumer.accept(fieldInfos);
        }
    }

    default void parseChangeFieldName(JSONObject jsonObject, BiConsumer<String,String> biConsumer) {
        JSONObject jo = jsonObject.getJSONObject("changeFieldName");
        if (jo != null && !jo.isEmpty()) {
            String oldFieldName = jo.getString("oldFieldName");
            String newFeildName = jo.getString("newFieldName");
            if (oldFieldName != null && !oldFieldName.isEmpty() && newFeildName != null && !newFeildName.isEmpty()) {
                biConsumer.accept(oldFieldName,newFeildName);
            }
        }
    }


    default void parseExchangeFieldOrder(JSONObject jsonObject, BiConsumer<String,String> biConsumer) {
        JSONObject exchangeFieldOrderJO = jsonObject.getJSONObject("exchangeFieldOrder");
        if (exchangeFieldOrderJO != null && !exchangeFieldOrderJO.isEmpty()) {
            String fieldName1 = exchangeFieldOrderJO.getString("fieldName1");
            String fieldName2 = exchangeFieldOrderJO.getString("fieldName2");
            biConsumer.accept(fieldName1,fieldName2);
        }
    }

    default void parseImportFieldState(JSONObject jsonObject,BiConsumer<String,Boolean> biConsumer) {
        JSONObject importFieldStateJO = jsonObject.getJSONObject("importFieldState");
        if (importFieldStateJO != null && !importFieldStateJO.isEmpty()) {
            String old = importFieldStateJO.getString("oldFieldName");
            boolean isExclude = importFieldStateJO.getBooleanValue("excludeField");
            biConsumer.accept(old,isExclude);
        }
    }


    default <S extends ImportSetting,D extends ImportDataInfo>  S parse(JSONObject importSettingJO, Class<S> clazz, BiConsumer<S,JSONObject> setImport, Class<D> dataInfoClazz , BiConsumer<D,JSONObject> setDataInfo) {
        JSONObject basePartJO = importSettingJO.getJSONObject("basePart");
        String basePart = basePartJO == null ? "{}": basePartJO.toJSONString();
        ParserConfig parserConfig = ConversionConfig.getParseConfig();
        S importSetting = JSON.parseObject(basePart, clazz, parserConfig);
        JSONObject jsonObject = JSON.parseObject(basePart);
        JSONObject scalingFactor = jsonObject.getJSONObject("scalingFactor");
        if (scalingFactor != null) {
            double ratioX = scalingFactor.getDoubleValue("ratioX");
            double ratioY = scalingFactor.getDoubleValue("ratioY");
            double ratioZ = scalingFactor.getDoubleValue("ratioZ");
            importSetting.setScalingFactor(ratioX,ratioY,ratioZ);
        }
        setImport.accept(importSetting,jsonObject);
        if (dataInfoClazz == null) {
            return importSetting;
        }
        JSONArray jsonArray = importSettingJO.getJSONArray("dataInfosPart");
        if (jsonArray == null || jsonArray.size() == 0) {
            return importSetting;
        }
        ImportDataInfos targetDataInfos = importSetting.getTargetDataInfos(null);
        for (int i = 0; i < jsonArray.size() && i < targetDataInfos.getCount(); i++) {
            D importDataInfo = dataInfoClazz.cast(targetDataInfos.get(i));
            JSONObject dataInfoJsonObj = jsonArray.getJSONObject(i);
            String targetName = dataInfoJsonObj.getString("targetName");
            if (targetName != null && !targetName.isEmpty()) {
                importDataInfo.setTargetName(targetName);
            }
            setDataInfo.accept(importDataInfo,dataInfoJsonObj);
        }
        importSetting.setTargetDataInfos(targetDataInfos);
        return importSetting;
    }

}

