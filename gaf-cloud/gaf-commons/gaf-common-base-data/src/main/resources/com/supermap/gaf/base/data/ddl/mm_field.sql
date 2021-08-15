-- liquibase formatted sql logicalFilePath:ddl/mm_field

-- changeset SYS:20210813-3
CREATE TABLE mm_field (
    field_id varchar(36) PRIMARY KEY NOT NULL,
    table_id varchar(36) NOT NULL,
    field_name varchar(255) NOT NULL,
    field_alias varchar(255) NULL,
    field_type varchar(50) NOT NULL,
    field_length int2 NULL,
    field_precision int2 NULL,
    field_default varchar(2000) NULL,
    field_not_null bool NOT NULL DEFAULT FALSE,
    field_primary_key bool NOT NULL DEFAULT FALSE,
    sort_sn int2 NOT NULL DEFAULT 1,
    description text NULL,
    created_time timestamp NOT NULL DEFAULT now(),
    created_by varchar(255) NULL,
    updated_time timestamp NULL,
    updated_by varchar(255) NULL
);
COMMENT ON TABLE mm_field IS '数据模型管理-字段表';
COMMENT ON COLUMN mm_field.field_id IS '主键';
COMMENT ON COLUMN mm_field.table_id IS '逻辑表id';
COMMENT ON COLUMN mm_field.field_name IS '字段名称';
COMMENT ON COLUMN mm_field.field_alias IS '字段别名';
COMMENT ON COLUMN mm_field.field_type IS '字段类型';
COMMENT ON COLUMN mm_field.field_length IS '字段长度';
COMMENT ON COLUMN mm_field.field_precision IS '字段精度';
COMMENT ON COLUMN mm_field.field_default IS '字段默认值';
COMMENT ON COLUMN mm_field.field_not_null IS '字段是否非空';
COMMENT ON COLUMN mm_field.field_primary_key IS '字段是否是主键';
COMMENT ON COLUMN mm_field.sort_sn IS '排序';
COMMENT ON COLUMN mm_field.description IS '描述';
COMMENT ON COLUMN mm_field.created_time IS '创建时间';
COMMENT ON COLUMN mm_field.created_by IS '创建人';
COMMENT ON COLUMN mm_field.updated_time IS '更新时间';
COMMENT ON COLUMN mm_field.updated_by IS '更新人';