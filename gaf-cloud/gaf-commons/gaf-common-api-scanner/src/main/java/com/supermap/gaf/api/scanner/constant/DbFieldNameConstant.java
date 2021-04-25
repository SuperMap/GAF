package com.supermap.gaf.api.scanner.constant;

/**
 * 数据库字段名常量
 * 用于查询数据库，指定字段名
 */
public final class DbFieldNameConstant {
    private DbFieldNameConstant() {
    }

    public static final String NAME_EN = "name_en";
    public static final String NAME = "name";
    public static final String NAME_CN = "name_cn";

    //------------------ 分页字段常量
    public static final String PAGE_LIST = "pageList";
    public static final String TOTAL_COUNT = "totalCount";


    //----------------- 上级关系字段
    public static final String PARENT_ID = "parent_id";


    //----------------- auth_department 表
    public static final String DEPARTMENT_ID = "department_id";
    public static final String DEPARTMENT_NAME = "department_name";


    //------------------ auth_post 表
    public static final String POST_ID = "post_id";
    public static final String POST_NAME = "post_name";

    //----------------- auth_role 表
    public static final String ROLE_NAME = "role_name";
    public static final String ROLE_CATALOG_ID = "role_catalog_id";


    //----------------- auth_user 表
    public static final String USER_NAME = "name";
    public static final String USER_EMAIL = "email";

    //----------------- sys_catalog 表
    public static final String SYS_COMPONENT_ID = "sys_component_id";

    //----------------- auth_p3_mapping_rule 表
    public static final String P3_MAPPING_TYPE = "mapping_type";

}
