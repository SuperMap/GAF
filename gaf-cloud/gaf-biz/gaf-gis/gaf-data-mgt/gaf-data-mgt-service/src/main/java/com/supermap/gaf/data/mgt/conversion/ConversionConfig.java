/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.conversion;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.supermap.data.*;
import com.supermap.data.conversion.*;
import com.supermap.gaf.data.mgt.conversion.deserializer.EnumDeserializer;
import com.supermap.gaf.data.mgt.conversion.deserializer.PrjCoordSysDeserializer;
import com.supermap.gaf.data.mgt.conversion.serializer.EnumSerializer;
import com.supermap.gaf.data.mgt.conversion.serializer.PrjCoordSysSerializer;

/**
 *
 * @author wxl
 * @since 2021/8/1
 */
public class ConversionConfig {
    private static  final ParserConfig PARSER_CONFIG = new ParserConfig();

    private static final SerializeConfig SERIALIZE_CONFIG  = new SerializeConfig();
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
        PARSER_CONFIG.getDeserializers().put(PrjCoordSys.class, PrjCoordSysDeserializer.instance);

        SERIALIZE_CONFIG.put(EncodeType .class, EnumSerializer.instance);
        SERIALIZE_CONFIG.put(ImportMode .class, EnumSerializer.instance);
        SERIALIZE_CONFIG.put(Charset.class, EnumSerializer.instance);
        SERIALIZE_CONFIG.put(IgnoreMode.class, EnumSerializer.instance);
        SERIALIZE_CONFIG.put(DatasetType.class, EnumSerializer.instance);
        SERIALIZE_CONFIG.put(FieldType.class, EnumSerializer.instance);
        SERIALIZE_CONFIG.put(MultiBandImportMode.class, EnumSerializer.instance);
        SERIALIZE_CONFIG.put(ObjRotateOption.class, EnumSerializer.instance);
        SERIALIZE_CONFIG.put(FileType.class, EnumSerializer.instance);
        SERIALIZE_CONFIG.put(GJBLayerType.class, EnumSerializer.instance);
        SERIALIZE_CONFIG.put(VCTVersion.class, EnumSerializer.instance);
        SERIALIZE_CONFIG.put(PrjCoordSys.class, PrjCoordSysSerializer.instance);
    }

    public static ParserConfig getParseConfig() {
        return PARSER_CONFIG;
    }

    public static SerializeConfig getSerializeConfig() {
        return SERIALIZE_CONFIG;
    }
}
