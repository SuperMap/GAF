package com.supermap.gaf.data.mgt.conversion.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;
import com.supermap.data.PrjCoordSys;

import java.lang.reflect.Type;
import java.math.BigDecimal;

/**
 * @author wxl
 * @since 2021/7/30
 */
public class PrjCoordSysDeserializer implements ObjectDeserializer {

    public final static PrjCoordSysDeserializer instance = new PrjCoordSysDeserializer();

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        final JSONLexer lexer = parser.lexer;

        final int token = lexer.token();

        if (token == JSONToken.NULL) {
            lexer.nextToken(JSONToken.COMMA);
            return null;
        }

        Integer intObj;
        try {
            if (token == JSONToken.LITERAL_INT) {
                int val = lexer.intValue();
                lexer.nextToken(JSONToken.COMMA);
                intObj = val;
            } else if (token == JSONToken.LITERAL_FLOAT) {
                BigDecimal decimalValue = lexer.decimalValue();
                lexer.nextToken(JSONToken.COMMA);
                intObj = decimalValue.intValue();
            } else {
                if (token == JSONToken.LBRACE) {
                    JSONObject jsonObject = new JSONObject(true);
                    parser.parseObject(jsonObject);
                    intObj = TypeUtils.castToInt(jsonObject);
                } else {
                    Object value = parser.parse();
                    intObj = TypeUtils.castToInt(value);
                }
            }
        } catch (Exception ex) {
            throw new JSONException("parse type PrjCoordSys error, field : " + fieldName, ex);
        }
        PrjCoordSys prjCoordSys = PrjCoordSys.fromEPSG(intObj);
        System.out.println("fieldName = " + fieldName);
        System.out.println("getEPSGCode = " + prjCoordSys.getEPSGCode());
        System.out.println("name = " + prjCoordSys.getName());
        return (T) prjCoordSys;
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}
