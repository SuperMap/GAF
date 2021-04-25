/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
*/
package com.supermap.gaf.analysis.utils;

import com.alibaba.fastjson.JSON;
import com.supermap.data.CoordSysTransMethod;
import com.supermap.data.CoordSysTranslator;
import com.supermap.data.PrjCoordSys;
import com.supermap.data.Toolkit;
import com.supermap.gaf.utils.LogUtil;
import com.supermap.services.components.commontypes.Geometry;
import com.supermap.services.providers.util.CommontypesConversion;
import org.slf4j.Logger;

import java.util.List;

/**
 * @date:2021/3/25
 * @author dqc
 */
public class GeometryUtil {
    
    private static Logger logger = LogUtil.getLocLogger(GeometryUtil.class);
    
    /**
     * 转化投影坐标(iobject)
     * @param geometry
     * @param sourceCode
     * @param targetCode
     */
    public static com.supermap.data.Geometry transUgoGeometry(com.supermap.data.Geometry geometry, int sourceCode, int targetCode){
        if (sourceCode == targetCode){
            return geometry;
        }
        CoordSysTranslator.convert(geometry, PrjCoordSys.fromEPSG(sourceCode),PrjCoordSys.fromEPSG(targetCode),null, CoordSysTransMethod.MTH_Prj4);
        return geometry;
    }


    /**
     * 批量转化投影坐标(iobject)
     * @param geometries
     * @param sourceCode
     * @param targetCode
     */
    public static List<com.supermap.data.Geometry> transUgoGeometries(List<com.supermap.data.Geometry> geometries,int sourceCode,int targetCode){
        if (sourceCode == targetCode){
            return geometries;
        }
        for (com.supermap.data.Geometry geo : geometries){
            GeometryUtil.transUgoGeometry(geo,sourceCode,targetCode);
        }
        return geometries;
    }

    /**
     * 转化投影坐标(iserver)
     * @param geometry
     * @param sourceCode
     * @param targetCode
     */
    public static Geometry transGeometry(Geometry geometry, int sourceCode, int targetCode){
        if (sourceCode == targetCode){
            return geometry;
        }
        com.supermap.data.Geometry ugoGeometry = CommontypesConversion.getUGOGeometry(geometry);
        CoordSysTranslator.convert(ugoGeometry, PrjCoordSys.fromEPSG(sourceCode),PrjCoordSys.fromEPSG(targetCode),null, CoordSysTransMethod.MTH_Prj4);
        return CommontypesConversion.getGeometry(ugoGeometry);
    }


    /**
     * 转化投影坐标(iserver)
     * @param geometries
     * @param sourceCode
     * @param targetCode
     */
    public static List<Geometry> transGeometries(List<Geometry> geometries, int sourceCode, int targetCode){
        if (sourceCode == targetCode){
            return geometries;
        }
        for (int i = 0; i < geometries.size(); i++) {
            Geometry geo = geometries.get(i);
            geometries.set(i,transGeometry(geo,sourceCode,targetCode));
        }
        return geometries;
    }

}
