/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.access.util;

import com.supermap.gaf.data.access.commontypes.SphericalAreaParasCGCS2000;
import com.supermap.gaf.data.access.commontypes.TJDWTypeEnum;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * @program: app-landuse
 * @description: 算术工具类
 * @author: lidong
 * @date:2021/3/25
 * @create: 2018/11/14
 */
public class MathematicsUtils {
    // 默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;

    /**
     * double类型相加，防止精度缺失处理
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double doubleSum(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.add(bd2).doubleValue();
    }

    /**
     * double类型相减
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double doubleSubtract(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.subtract(bd2).doubleValue();
    }

    /**
     * double类型相乘
     *
     * @param d1
     * @param d2
     * @return
     */
    public static double doubleMultiply(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.multiply(bd2).doubleValue();
    }

    /**
     * double类型相除
     *
     * @param d1
     * @param d2
     * @param scale
     * @return
     */
    public static double doubleDivide(double d1, double d2, int scale) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.divide(bd2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * double类型四舍五入
     *
     * @param d1
     * @param scale
     * @return
     */
    public static double round(double d1, int scale) {
        BigDecimal b = new BigDecimal(Double.toString(d1));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * double转BigDecimal
     *
     * @param d1
     * @return
     */
    public static double getBigDecimal(double d1) {
        BigDecimal b = new BigDecimal(Double.toString(d1));
        return b.doubleValue();
    }

    /**
     * double判等
     *
     * @param d1
     * @param d2
     * @return
     */
    public static boolean isDoubleEquals(double d1, double d2) {
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.compareTo(bd2) == 0;
    }

    /**
     * 根据统计单位换算转换比率
     *
     * @param tjdw    统计单位
     * @param refTJDW 参考统计单位
     * @return
     */
    public static double getConversionRatio(TJDWTypeEnum tjdw, TJDWTypeEnum refTJDW, int scale) {
        double conversionRatio = 1;
        if (tjdw.equals(refTJDW)) {
            conversionRatio = 1;
        } else {
            // 公顷转亩
            if (TJDWTypeEnum.GQ == tjdw && TJDWTypeEnum.MU == refTJDW) {
                conversionRatio = doubleDivide(10000, (doubleDivide(2000, 3, scale)), scale);
            }
            // 亩转公顷
            if (TJDWTypeEnum.MU == tjdw && TJDWTypeEnum.GQ == refTJDW) {
                conversionRatio = doubleDivide(doubleDivide(2000, 3, scale), 10000, scale);
            }
            // 亩转平方米
            if (TJDWTypeEnum.MU == tjdw && TJDWTypeEnum.PM == refTJDW) {
                conversionRatio = doubleDivide(2000, 3, scale);
            }
            // 平方米转亩
            if (TJDWTypeEnum.PM == tjdw && TJDWTypeEnum.MU == refTJDW) {
                conversionRatio = doubleDivide(1, (doubleDivide(2000, 3, scale)), scale);
            }
            // 平方米转公顷
            if (TJDWTypeEnum.PM == tjdw && TJDWTypeEnum.GQ == refTJDW) {
                conversionRatio = doubleDivide(1, 10000, scale);
            }
            // 公顷平方米
            if (TJDWTypeEnum.GQ == tjdw && TJDWTypeEnum.PM == refTJDW) {
                conversionRatio = 10000;
            }
        }
        return conversionRatio;
    }

    /**
     * 度转换为弧度
     *
     * @param A_0 度
     * @return 弧度
     */
    public static double transDegreeToArc(double A_0) {
        return ((A_0 * SphericalAreaParasCGCS2000.PI) / 180.0);
    }

    /// <summary>
    /// 弧度转换成秒，秒后保留6位小数，小数点后第7位四舍五入；
    /// </summary>
    /// <param name="A"></param>
    /// <returns></returns>
    public static double transArcToSec(double A) {
        //double dSec = A * 180.0 * 3600 / Math.PI;
        double dSec = A * SphericalAreaParasCGCS2000.p; // 国图使用文档中的参数，感觉应该使用更精确的计算值
        dSec = round(dSec, 6);
        return dSec;
    }

    /// <summary>
    /// 秒转换为弧度
    /// </summary>
    /// <param name="sec"></param>
    /// <returns></returns>
    public static double transSecToArc(double sec) {
        double arc = 0;
        //arc = sec * this.m_Paras.PI / (3600 * 180.0);
        arc = sec / SphericalAreaParasCGCS2000.p; // 国图使用文档中的参数，感觉应该使用更精确的计算值
        return arc;
    }

    /// <summary>
    /// 度转成秒
    /// </summary>
    /// <param name="dRet">度</param>
    /// <returns>double</returns>
    public static double transDegreeToSec(double dRet) {
        double dSec;
        dSec = dRet * 3600;
        dSec = round(dSec, 6);
        return dSec;
    }

    /**
     * double数值过大时,按字符串显示,而不使用科学计数法显示
     *
     * @param d
     * @return
     */
    public static String doubleToString(double d) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        return nf.format(d);
    }

}
