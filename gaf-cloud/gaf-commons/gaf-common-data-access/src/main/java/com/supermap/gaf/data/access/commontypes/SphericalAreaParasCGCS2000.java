package com.supermap.gaf.data.access.commontypes;

public class SphericalAreaParasCGCS2000 {

    public static final double PI = 3.14159265358979;    //PI
    public static final double param_a = 0.0;
    public static final double param_d = 0.000000000001;
    //---------------------------------------------------------------------------------
    public static final double p = 206264.8062471;      //180*3600/PI
    public static final double a = 6378137.0;           //长轴半径
    //public double b = 6356752.314140356;   //短轴半径
    public static final double b = 6356752.31414036;   //短轴半径 测评参考资料修改
    public static final double alpha = 0.0033528106811823188;   //椭球偏心率 1/ 298.257222101
    public static final double e2 = 0.00669438002290;                //椭球第一偏心率平方
    public static final double e21= 0.00673949677548;                //椭球第二偏心率平方
    public static final double c  = 6399593.62586;                   //极点子午圈曲率半径
    //--------------------------------------------------------------------------------
    public static final double e4 = Math.pow(e2, 2);
    public static final double e6 = Math.pow(e2, 3);
    public static final double e8 = Math.pow(e2, 4);
    public static final double K0 = 0.000000157048761144159;
    public static final double K1 = 0.00505250178820567;
    public static final double K2 = 0.0000298472900956587;
    public static final double K3 = 0.000000241626669230084;
    public static final double K4 = 0.00000000222241238938534;
    public static final double A1 = 1.0 + (3.0 / 6.0) * e2 + (30.0 / 80.0) * e4 + (35.0 / 112.0) * e6 + (630.0 / 2304.0) * e8;;
    public static final double B1 = (1.0 / 6.0) * e2 + (15.0 / 80.0) * e4 + (21.0 / 112.0) * e6 + (420.0 / 2304.0) * e8;
    public static final double C1 = (3.0 / 80.0) * e4 + (7.0 / 112.0) * e6 + (180.0 / 2304.0) * e8;
    public static final double D1 = (1.0 / 112.0) * e6 + (45.0 / 2304.0) * e8;
    public static final double E1 = (5.0 / 2304.0) * e8;
}
