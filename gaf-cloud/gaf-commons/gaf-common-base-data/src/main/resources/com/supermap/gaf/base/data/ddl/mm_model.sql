-- liquibase formatted sql logicalFilePath:ddl/mm_model

-- changeset SYS:20210813-0
CREATE TABLE mm_model (
    model_id varchar(36) PRIMARY KEY NOT NULL,
    model_name varchar(255) NOT NULL,
    model_type varchar(50) NOT NULL,
    model_code varchar(255) NOT NULL,
    sort_sn int2 NOT NULL DEFAULT 1,
    description text NULL,
    created_time timestamp NOT NULL DEFAULT now(),
    created_by varchar(255) NULL,
    updated_time timestamp NULL,
    updated_by varchar(255) NULL
);
COMMENT ON TABLE mm_model IS '数据模型管理-模型表';
COMMENT ON COLUMN mm_model.model_id IS '主键';
COMMENT ON COLUMN mm_model.model_name IS '模型名称';
COMMENT ON COLUMN mm_model.model_type IS '模型类型';
COMMENT ON COLUMN mm_model.model_code IS '模型标识';
COMMENT ON COLUMN mm_model.sort_sn IS '排序';
COMMENT ON COLUMN mm_model.description IS '描述';
COMMENT ON COLUMN mm_model.created_time IS '创建时间';
COMMENT ON COLUMN mm_model.created_by IS '创建人';
COMMENT ON COLUMN mm_model.updated_time IS '更新时间';
COMMENT ON COLUMN mm_model.updated_by IS '更新人';