package com.supermap.gaf.data.mgt.conversion;

import com.alibaba.fastjson.parser.ParserConfig;
import com.supermap.data.*;
import com.supermap.data.conversion.IgnoreMode;
import com.supermap.data.conversion.ImportMode;
import com.supermap.data.conversion.MultiBandImportMode;
import com.supermap.data.conversion.ObjRotateOption;
import com.supermap.gaf.data.mgt.conversion.deserializer.EnumDeserializer;
import com.supermap.gaf.data.mgt.conversion.deserializer.PrjCoordSysDeserializer;

/**
 *
 * @author wxl
 * @since 2021/8/1
 */
public class ConversionConfig {
    private static  final ParserConfig parserConfig = new ParserConfig();

    static {
        parserConfig.getDeserializers().put(EncodeType .class, EnumDeserializer.instance);
        parserConfig.getDeserializers().put(ImportMode .class, EnumDeserializer.instance);
        parserConfig.getDeserializers().put(Charset.class, EnumDeserializer.instance);
        parserConfig.getDeserializers().put(IgnoreMode.class, EnumDeserializer.instance);
        parserConfig.getDeserializers().put(DatasetType.class, EnumDeserializer.instance);
        parserConfig.getDeserializers().put(FieldType.class, EnumDeserializer.instance);
        parserConfig.getDeserializers().put(MultiBandImportMode.class, EnumDeserializer.instance);
        parserConfig.getDeserializers().put(ObjRotateOption.class, EnumDeserializer.instance);
        parserConfig.getDeserializers().put(PrjCoordSys .class, PrjCoordSysDeserializer.instance);
    }

    public static ParserConfig getParseConfig() {
        return parserConfig;
    }

}
