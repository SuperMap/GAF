/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.conversion;

import com.alibaba.fastjson.parser.ParserConfig;
import com.supermap.data.*;
import com.supermap.data.conversion.*;
import com.supermap.gaf.data.mgt.conversion.deserializer.EnumDeserializer;
import com.supermap.gaf.data.mgt.conversion.deserializer.PrjCoordSysDeserializer;

/**
 * 数据导出导出配置
 * 提供fastjson的parseConfig对象 ,parseConfig对象增加了其他的反序列化器,用于json字符串的反序列化
 * @author wxl
 * @since 2021/8/1
 */
public class ConversionConfig {
    private static  final ParserConfig PARSER_CONFIG = new ParserConfig();

    static {
        PARSER_CONFIG.getDeserializers().put(EncodeType .class, EnumDeserializer.instance);
        PARSER_CONFIG.getDeserializers().put(ImportMode .class, EnumDeserializer.instance);
        PARSER_CONFIG.getDeserializers().put(Charset.class, EnumDeserializer.instance);
        PARSER_CONFIG.getDeserializers().put(IgnoreMode.class, EnumDeserializer.instance);
        PARSER_CONFIG.getDeserializers().put(DatasetType.class, EnumDeserializer.instance);
        PARSER_CONFIG.getDeserializers().put(FieldType.class, EnumDeserializer.instance);
        PARSER_CONFIG.getDeserializers().put(MultiBandImportMode.class, EnumDeserializer.instance);
        PARSER_CONFIG.getDeserializers().put(ObjRotateOption.class, EnumDeserializer.instance);
        PARSER_CONFIG.getDeserializers().put(FileType.class, EnumDeserializer.instance);
        PARSER_CONFIG.getDeserializers().put(GJBLayerType.class, EnumDeserializer.instance);
        PARSER_CONFIG.getDeserializers().put(VCTVersion.class, EnumDeserializer.instance);
        PARSER_CONFIG.getDeserializers().put(EngineType.class, EnumDeserializer.instance);
        PARSER_CONFIG.getDeserializers().put(PrjCoordSys.class, PrjCoordSysDeserializer.instance);

        PARSER_CONFIG.getDeserializers().put(BlockSizeOption.class, EnumDeserializer.instance);
        PARSER_CONFIG.getDeserializers().put(PixelFormat.class, EnumDeserializer.instance);
        PARSER_CONFIG.getDeserializers().put(DatasetType.class, EnumDeserializer.instance);
    }

    public static ParserConfig getParseConfig() {
        return PARSER_CONFIG;
    }

}
