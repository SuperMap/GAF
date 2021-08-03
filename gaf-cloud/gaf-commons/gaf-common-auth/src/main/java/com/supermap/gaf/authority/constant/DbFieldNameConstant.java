package com.supermap.gaf.authority.constant;

/**
 * 数据库字段名常量
 * 用于查询数据库，指定字段名
 */
public final class DbFieldNameConstant {


    private DbFieldNameConstant() {
    }
    // ---------------- 通用字段
    /**
     * 通用字段 表示同级排序序号 从1开始的正整数
     */
    public static final String SORT_SN = "sort_sn";

    /**
     * 通用字段 表示租户id
     */
    public static final String TENANT_ID = "tenant_id";
    /**
     * 通用字段 上级关系字段
     */
    public static final String PARENT_ID = "parent_id";
    /**
     * 状态 逻辑删除字段 通用字段
     */
    public static final String STATUS = "status";
    /**
     * 目录id 通用字段
     */
    public static final String CATALOG_ID = "catalog_id";
    /**
     * 更新时间 通用字段
     */
    public static final String UPDATED_TIME = "updated_time";

    //------------------ 分页字段常量
    public static final String PAGE_LIST = "pageList";
    public static final String TOTAL_COUNT = "totalCount";

    public static final String NAME = "name";

    //----------------- sys_catalog 系统目录表
    /**
     * 目录表表名
     */
    public static final String SYS_CATALOG = "sys_catalog";

    /**
     * 目录类型
     */
    public static final String TYPE = "type";


    //----------------- auth_department 表
    public static final String DEPARTMENT_NAME = "department_name";


    //------------------ auth_post 表
    public static final String POST_ID = "post_id";
    public static final String POST_NAME = "post_name";

    //----------------- auth_role 表
    public static final String ROLE_NAME = "role_name";
    public static final String ROLE_CATALOG_ID = "role_catalog_id";


    //----------------- auth_p3_mapping_rule 表
    public static final String P3_MAPPING_TYPE = "mapping_type";

    // ------------------sys_dict表
    /**
     * 字典id
     */
    public static final String DATA_DICT_ID = "data_dict_id";

    /**
     * 字典表 表名
     */
    public static final String SYS_DICT = "sys_dict";
    /**
     * 同级排序的序号
     */
    public static final String SEQ = "seq";

    /**
     * 父id
     */
    public static final String PID = "pid";
    // ------------------auth_tenant表
    public static final String ADMIN_ID = "admin_id";

}
