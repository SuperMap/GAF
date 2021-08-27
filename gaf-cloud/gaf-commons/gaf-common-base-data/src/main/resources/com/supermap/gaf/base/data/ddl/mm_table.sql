-- liquibase formatted sql logicalFilePath:ddl/mm_table

-- changeset SYS:20210813-1
CREATE TABLE mm_table (
    table_id varchar(36) PRIMARY KEY NOT NULL,
    table_name varchar(255) NOT NULL,
    model_id varchar(36) NOT NULL,
    sdx_info text NULL,
    sort_sn int2 NOT NULL DEFAULT 1,
    description text NULL,
    created_time timestamp NOT NULL DEFAULT now(),
    created_by varchar(255) NULL,
    updated_time timestamp NULL,
    updated_by varchar(255) NULL
);
COMMENT ON TABLE mm_table IS '数据模型管理-逻辑表';
COMMENT ON COLUMN mm_table.table_id IS '主键';
COMMENT ON COLUMN mm_table.table_name IS '逻辑表名称';
COMMENT ON COLUMN mm_table.model_id IS '模型id';
COMMENT ON COLUMN mm_table.sdx_info IS '空间数据源sdx数据集信息';
COMMENT ON COLUMN mm_table.sort_sn IS '排序';
COMMENT ON COLUMN mm_table.description IS '描述';
COMMENT ON COLUMN mm_table.created_time IS '创建时间';
COMMENT ON COLUMN mm_table.created_by IS '创建人';
COMMENT ON COLUMN mm_table.updated_time IS '更新时间';
COMMENT ON COLUMN mm_table.updated_by IS '更新人';