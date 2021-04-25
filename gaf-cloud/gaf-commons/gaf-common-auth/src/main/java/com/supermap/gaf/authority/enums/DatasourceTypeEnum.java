//package com.supermap.gaf.authority.enums;
//
///**
// * 数据库类型
// * @author wxl
// */
//public enum DatasourceTypeEnum {
//    /**
//     * 未知 ，用于占位
//     */
//    UNKNOWN,
//    /**
//     * POSTGRESQL
//     */
//    POSTGRESQL,
//    /**
//     * HBASE 暂时不使用
//     */
//    HBASE,
//    /**
//     * 文件型数据库 暂时不使用
//     */
//    FILE,
//    /**
//     * MYSQL
//     */
//    MYSQL,
//    /**
//     * ORACLE
//     */
//    ORACLE,
//    /**
//     * SQLSERVER
//     */
//    SQLSERVER;
//
//    public static DatasourceTypeEnum getByOrdinal(int ordinal) {
//        DatasourceTypeEnum[] values = DatasourceTypeEnum.values();
//        for (DatasourceTypeEnum value : values) {
//            if (value.ordinal() == ordinal) {
//                return value;
//            }
//        }
//        return UNKNOWN;
//    }
//}
