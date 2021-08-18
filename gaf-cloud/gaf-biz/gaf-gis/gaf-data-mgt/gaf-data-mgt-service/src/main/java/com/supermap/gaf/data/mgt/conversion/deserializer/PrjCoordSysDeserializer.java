package com.supermap.gaf.data.mgt.conversion.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.supermap.data.PrjCoordSys;
import com.supermap.gaf.data.mgt.util.PrjCoordSysUtil;

import java.lang.reflect.Type;

/**
 * @author wxl
 * @since 2021/7/30
 */
public class PrjCoordSysDeserializer implements ObjectDeserializer {

    public final static PrjCoordSysDeserializer instance = new PrjCoordSysDeserializer();

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONLexer lexer = parser.getLexer();
        String val;
        if(lexer.token() == JSONToken.LITERAL_STRING) {
            val = lexer.stringVal();
            lexer.nextToken(JSONToken.COMMA);
        } else {
            Object value = parser.parse();
            if (value == null) {
                return null;
            }
            val = value.toString();
        }
        PrjCoordSys prjCoordSys = PrjCoordSysUtil.parse(val);
        return (T) prjCoordSys;
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }
}
