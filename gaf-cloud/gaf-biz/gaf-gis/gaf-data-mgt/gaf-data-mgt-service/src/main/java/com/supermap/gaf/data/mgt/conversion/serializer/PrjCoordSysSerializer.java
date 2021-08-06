/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.mgt.conversion.serializer;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.supermap.data.PrjCoordSys;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author wxl
 * @since 2021/8/3
 */
public class PrjCoordSysSerializer implements ObjectSerializer {

    public final static PrjCoordSysSerializer instance = new PrjCoordSysSerializer();

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        PrjCoordSys prjCoordSys = (PrjCoordSys) object;
        out.write(prjCoordSys.getEPSGCode());
    }
}
