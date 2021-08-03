/*
 * Copyright© 2000 - 2021 SuperMap Software Co.Ltd. All rights reserved.
 * This program are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution and is available at http://www.apache.org/licenses/LICENSE-2.0.html.
 */
package com.supermap.gaf.data.access.util;

import com.supermap.data.*;
import com.supermap.gaf.data.access.commontypes.SphericalAreaParasCGCS2000;
import org.omg.CORBA.DoubleHolder;


/**
 * @author:yj
 * @date:2021/3/25
 */
public class AreaCalculate {

    /**
     * 椭球面上任意封闭图斑椭球面积
     *
     * @param region      面对象
     * @param prjCoordSys 投影信息
     * @return 椭球面积
     */
    public static double GetPreciseArea(GeoRegion region, PrjCoordSys prjCoordSys) {
        double area = 0.0;
        PrjParameter parameter = prjCoordSys.getPrjParameter();
        Unit unit = prjCoordSys.getCoordUnit();
        //GeoSpatialRefType geoSpatialRefType = prjCoordSys.getGeoCoordSys().getGeoSpatialRefType();
        String typeStr = prjCoordSys.getType().toString();

        double dCenterLine = parameter.getCentralMeridian();
        boolean bIsJWD = (unit == Unit.DEGREE || unit == Unit.MINUTE || unit == Unit.SECOND) ? true : false;
        double dStripNum = dCenterLine / 3;
        String type = "China2000Gk";
        if (typeStr.indexOf(type) != -1) {
            dStripNum = Double.valueOf(typeStr.substring(type.length(), type.length() + 2));
        }

        //将中央子午线经度值转换为弧度
        double dCenterLineArc = MathematicsUtils.transDegreeToArc(dCenterLine);
        int[] partTopo = region.getPartTopo();
        //循环当前图形的拓扑子对象
        for (int i = 0; i < region.getPartCount(); i++) {
            if (partTopo[i] == 1) {
                //如果是岛，则加上
                double dAreaX = 0.0;
                if (bIsJWD) {
                    dAreaX = CalculateRingArea(region.getPart(i), dCenterLine);
                } else {
                    dAreaX = CalculateRingArea(region.getPart(i), dCenterLineArc, dStripNum);
                }
                area += dAreaX;
            } else {
                //如果是洞，则减去
                double areaHole = 0.0;
                if (bIsJWD) {
                    areaHole = CalculateRingArea(region.getPart(i), dCenterLine);
                } else {
                    areaHole = CalculateRingArea(region.getPart(i), dCenterLineArc, dStripNum);
                }
                area -= areaHole;
            }
        }
        return area;
    }

    /**
     * 椭球面上任意封闭图斑椭球面积
     *
     * @param pointCollection 点集合（投影坐标）
     * @param dCenterLine     中央子午线经度值
     * @param dStripNum       带号
     * @return 封闭图斑椭球面积
     */
    private static double CalculateRingArea(Point2Ds pointCollection, double dCenterLine, double dStripNum) {
        double areaSum = 0.0;
        int gsfs_xy_xsws = 4;
        int gsfs_xy_ncd_xsws = 6;
        //循环将封闭图斑内的梯形块面积累加，得到封闭图斑的椭球面积
        for (int i = 0; i < (pointCollection.getCount() - 1); i++) {
            //椭球面积计算时，如果线段长度 > 70 米，则需进行差点（均匀差点）计算，不分割只用于计算；
            //内插点个数等于线段长度除以70，结果取整，均匀差值；内插点的小数位数保留6位小数，小数点后第7位四舍五入；
            Point2D p1 = pointCollection.getItem(i);
            Point2D p2 = pointCollection.getItem(i + 1);

            //计算过程中高斯平面坐标X,Y要保留4位小数，小数点后第5位四舍五入...
            //高斯投影反解变换后的B，L以秒表示，保留小数点后6位，小数点后第7位四舍五入
            double x1 = p1.getX();
            double y1 = p1.getY();
            double x2 = p2.getX();
            double y2 = p2.getY();
            if (gsfs_xy_xsws > 0) {
                x1 = MathematicsUtils.round(x1, gsfs_xy_xsws);
                y1 = MathematicsUtils.round(y1, gsfs_xy_xsws);
                x2 = MathematicsUtils.round(x2, gsfs_xy_xsws);
                y2 = MathematicsUtils.round(y2, gsfs_xy_xsws);
            }

            //double distance = SpatialCommon.Distance(p1, p2); // 线段长度
            double dx = p2.getX() - p1.getX();
            double dy = p2.getY() - p1.getY();
            double dx2 = Math.pow(Math.abs(dx), 2);
            double dy2 = Math.pow(Math.abs(dy), 2);
            double distance = Math.sqrt(dx2 + dy2);
            int n = (int) distance / 70; //内插点个数，结果取整
            //double interval = distance / (n + 1); //均匀差值，每段的长度

            for (int j = 1; j <= n + 1; j++) {
                DoubleHolder B1 = new DoubleHolder();
                DoubleHolder L1 = new DoubleHolder();
                DoubleHolder B2 = new DoubleHolder();
                DoubleHolder L2 = new DoubleHolder();
                DoubleHolder DC = new DoubleHolder(dCenterLine);
                double x, y;
                if (j < n + 1) {
                    // 内插点计算时起点不四舍五入
                    x = p1.getX() + dx * j / (n + 1);
                    y = p1.getY() + dy * j / (n + 1);
                    // 内插点的小数位数保留6位小数
                    x = MathematicsUtils.round(x, gsfs_xy_ncd_xsws);
                    y = MathematicsUtils.round(y, gsfs_xy_ncd_xsws);
                } else { //最后一点为非内插点
                    x = x2;
                    y = y2;
                }

                //先进行高斯投影反算（x,y-->B,L）
                ComputeXYGeo(y1, x1, B1, L1, dCenterLine, dStripNum);
                ComputeXYGeo(y, x, B2, L2, dCenterLine, dStripNum);

                //测评要求：B、L由弧度转秒保留6位小数后，再转回弧度；
                BLArc2Sec2Arc(B1, B2, L1, L2, DC);

                double dArea = CalculateBLArea(B1.value, B2.value, L1.value, L2.value, DC.value);

                //汇总椭球面积
                areaSum += dArea;
                x1 = x;
                y1 = y;
            }
        }
        return Math.abs(areaSum);
    }

    /**
     * 椭球面上任意封闭图斑椭球面积
     *
     * @param pointCollection 点集合 （经纬度坐标）
     * @param dCenterLine     中央子午线经度值
     * @return 封闭图斑椭球面积
     */
    private static double CalculateRingArea(Point2Ds pointCollection, double dCenterLine) {
        double areaSum = 0.0;
        //循环将封闭图斑内的梯形块面积累加，得到封闭图斑的椭球面积
        for (int i = 0; i < (pointCollection.getCount() - 1); i++) {
            Point2D p1 = pointCollection.getItem(i);
            Point2D p2 = pointCollection.getItem(i + 1);

            double L1 = MathematicsUtils.transDegreeToArc(p1.getX());
            double B1 = MathematicsUtils.transDegreeToArc(p1.getY());
            double L2 = MathematicsUtils.transDegreeToArc(p2.getX());
            double B2 = MathematicsUtils.transDegreeToArc(p2.getY());

            double dArea = CalculateBLArea(B1, B2, L1, L2, MathematicsUtils.transDegreeToArc(dCenterLine));

            //汇总椭球面积
            areaSum += dArea;
        }
        return Math.abs(areaSum);
    }

    /**
     * 高斯投影反解变换（x,y-->B,L 公式）；计算过程中高斯平面坐标X,Y要保留三位小数，小数点后第四位四舍五入
     *
     * @param X           高斯投影坐标X值（平方米）
     * @param Y           高斯投影坐标Y值（平方米）
     * @param B           纬度（弧度）
     * @param L           经度（弧度）
     * @param dCenterLine 中央子午线经度值（弧度）
     * @param dStripNum   带号
     * @return
     */
    private static void ComputeXYGeo(double X, double Y, DoubleHolder B, DoubleHolder L, double dCenterLine, double dStripNum) {

        //        //计算过程中高斯平面坐标X,Y要保留4位小数，小数点后第5位四舍五入...
        //        //高斯投影反解变换后的B，L以秒表示，保留小数点后6位，小数点后第7位四舍五入
        //        int gsfs_xy_xsws = 4;
        //        if (gsfs_xy_xsws > 0)
        //        {
        //            X = MathematicsUtils.round(X, gsfs_xy_xsws);
        //            Y = MathematicsUtils.round(Y, gsfs_xy_xsws);
        //        }

        double y1 = 0;
        if (Y > 1000000) {
            //带带号
            y1 = (Y - 500000) - (dStripNum * 1000000);
        } else {
            //不带带号
            y1 = (Y - 500000);
        }
        double E = SphericalAreaParasCGCS2000.K0 * X;
        double SinE = Math.sin(E);
        double Bf = E + (Math.cos(E) * (
                (((SphericalAreaParasCGCS2000.K1 * SinE) - (SphericalAreaParasCGCS2000.K2 * Math.pow(SinE, 3))) + (SphericalAreaParasCGCS2000.K3 * Math
                        .pow(SinE, 5))) - (SphericalAreaParasCGCS2000.K4 * Math.pow(SinE, 7))));
        double t = Math.tan(Bf);
        double n2 = SphericalAreaParasCGCS2000.e21 * Math.pow(Math.cos(Bf), 2);
        double V = Math.sqrt(1.0 + n2);
        double N = SphericalAreaParasCGCS2000.c / V;  //C=a*a/b;
        double y1N = y1 / N;
        double V2t = Math.pow(V, 2) * t;
        double t2 = Math.pow(t, 2);
        B.value = ((Bf - ((V2t * Math.pow(y1N, 2)) / 2.0)) + ((((((5.0 + (3.0 * t2)) + n2) - ((9.0 * n2) * t2)) * V2t) * Math.pow(y1N, 4)) / 24.0)) - (
                ((((61.0 + (90.0 * t2)) + (45.0 * Math.pow(t2, 2))) * V2t) * Math.pow(y1N, 6)) / 720.0);
        double CosBf1 = 1 / Math.cos(Bf);
        L.value = (((CosBf1 * y1N) - (((((1.0 + (2.0 * t2)) + n2) * CosBf1) * Math.pow(y1N, 3)) / 6.0)) + (
                ((((((5.0 + (28.0 * t2)) + (24.0 * Math.pow(t2, 2))) + (6.0 * n2)) + ((8.0 * n2) * t2)) * CosBf1) * Math.pow(y1N, 5)) / 120.0)) + dCenterLine;
    }

    /**
     * B、L由弧度转秒保留6位小数后，再转回弧度
     */
    private static void BLArc2Sec2Arc(DoubleHolder B1, DoubleHolder B2, DoubleHolder L1, DoubleHolder L2, DoubleHolder dCenterLine) {
        //将反算后的B，L由弧度转换为秒，保留秒后6位小数；(三调测评要求)
        double B1_miao = MathematicsUtils.transArcToSec(B1.value);
        double L1_miao = MathematicsUtils.transArcToSec(L1.value);
        double B2_miao = MathematicsUtils.transArcToSec(B2.value);
        double L2_miao = MathematicsUtils.transArcToSec(L2.value);
        double dCenterLine_miao = MathematicsUtils.transArcToSec(dCenterLine.value);
        //再将B，L由秒转换为弧度，直接由弧度计算公式代入，不计算中间的弧度值
        B1.value = MathematicsUtils.transSecToArc(B1_miao);
        L1.value = MathematicsUtils.transSecToArc(L1_miao);
        B2.value = MathematicsUtils.transSecToArc(B2_miao);
        L2.value = MathematicsUtils.transSecToArc(L2_miao);
        dCenterLine.value = MathematicsUtils.transSecToArc(dCenterLine_miao);
    }

    /**
     * 计算梯形经纬度面积
     */
    private static double CalculateBLArea(double B1, double B2, double L1, double L2, double dCenterLine) {
        double dB21 = (B2 - B1) / 2.0;  //图块纬差的一半,单位：弧度
        double Bm = (B2 + B1) / 2.0; //图幅南北图廓的维度平均值,单位：弧度
        double dL21 = (L2 - L1) / 2.0; //未用到，单位：弧度

        //deltaL 为相邻两点大地经度的平均值与L0（指定的一条经线L0）的差,指定的经线可以是任意经线，可以为0，也可以为中央经线；
        //以下两种减与不减中央经线，计算结果差别甚微，误差基本精确到小数点后0.01-0.02左右；
        double deltaL0 = (L2 + L1) / 2.0; // 指定的经线L0是0；
        double deltaL1 = (L2 + L1) / 2.0 - dCenterLine; //指定的经线L0是中央经线；
        double deltaL = deltaL0;
        double num = 2.0 * SphericalAreaParasCGCS2000.b * SphericalAreaParasCGCS2000.b * deltaL;

        double Cos_aB21 = Math.cos(Bm);
        double Sin_dB21 = Math.sin(dB21);
        double[] numArray = new double[5];
        numArray[0] = num * SphericalAreaParasCGCS2000.A1 * Sin_dB21 * Cos_aB21;
        numArray[1] = num * SphericalAreaParasCGCS2000.B1 * Math.sin(3.0 * dB21) * Math.cos(3.0 * Bm);
        numArray[2] = num * SphericalAreaParasCGCS2000.C1 * Math.sin(5.0 * dB21) * Math.cos(5.0 * Bm);
        numArray[3] = num * SphericalAreaParasCGCS2000.D1 * Math.sin(7.0 * dB21) * Math.cos(7.0 * Bm);
        numArray[4] = num * SphericalAreaParasCGCS2000.E1 * Math.sin(9.0 * dB21) * Math.cos(9.0 * Bm);
        double dArea = numArray[0] - numArray[1] + numArray[2] - numArray[3] + numArray[4];
        return dArea;
    }
}
