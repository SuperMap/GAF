-- liquibase formatted sql logicalFilePath:ddl/mm_field_associate

-- changeset SYS:20210813-5
CREATE TABLE mm_field_associate (
    field_associate_id varchar(36) PRIMARY KEY NOT NULL,
    model_id varchar(36) NOT NULL,
    source_field_table_id varchar(36) NOT NULL,
    target_field_table_id varchar(36) NOT NULL,
    source_field_id varchar(36) NOT NULL,
    target_field_id varchar(36) NOT NULL,
    created_time timestamp NOT NULL DEFAULT now(),
    created_by varchar(255) NULL,
    updated_time timestamp NULL,
    updated_by varchar(255) NULL
);
COMMENT ON TABLE mm_field_associate IS '数据模型管理-字段关联表';
COMMENT ON COLUMN mm_field_associate.field_associate_id IS '主键';
COMMENT ON COLUMN mm_field_associate.model_id IS '模型id';
COMMENT ON COLUMN mm_field_associate.source_field_table_id IS '字段1逻辑表id';
COMMENT ON COLUMN mm_field_associate.target_field_table_id IS '字段2逻辑表id';
COMMENT ON COLUMN mm_field_associate.source_field_id IS '字段1字段表id';
COMMENT ON COLUMN mm_field_associate.target_field_id IS '字段2字段表id';
COMMENT ON COLUMN mm_field_associate.created_time IS '创建时间';
COMMENT ON COLUMN mm_field_associate.created_by IS '创建人';
COMMENT ON COLUMN mm_field_associate.updated_time IS '更新时间';
COMMENT ON COLUMN mm_field_associate.updated_by IS '更新人';