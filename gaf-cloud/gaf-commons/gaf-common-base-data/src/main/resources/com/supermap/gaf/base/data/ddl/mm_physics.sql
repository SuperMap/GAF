-- liquibase formatted sql logicalFilePath:ddl/mm_physics

-- changeset SYS:20210813-4
CREATE TABLE mm_physics (
    physics_id varchar(36) PRIMARY KEY NOT NULL,
    physics_name varchar(255) NULL,
    table_id varchar(36) NOT NULL,
    server varchar(2000) NOT NULL,
    db_name varchar(255) NULL,
    username varchar(255) NULL,
    password varchar(255) NULL,
    type varchar(255) NULL,
    created_time timestamp NOT NULL DEFAULT now(),
    created_by varchar(255) NULL,
    updated_time timestamp NULL,
    updated_by varchar(255) NULL
);
COMMENT ON TABLE mm_physics IS '数据模型管理-物理表';
COMMENT ON COLUMN mm_physics.physics_id IS '主键';
COMMENT ON COLUMN mm_physics.physics_name IS '物理表名';
COMMENT ON COLUMN mm_physics.table_id IS '逻辑表id';
COMMENT ON COLUMN mm_physics.server IS '数据库地址';
COMMENT ON COLUMN mm_physics.db_name IS '数据库名';
COMMENT ON COLUMN mm_physics.username IS '用户名';
COMMENT ON COLUMN mm_physics.password IS '密码';
COMMENT ON COLUMN mm_physics.type IS '数据源类型';
COMMENT ON COLUMN mm_physics.created_time IS '创建时间';
COMMENT ON COLUMN mm_physics.created_by IS '创建人';
COMMENT ON COLUMN mm_physics.updated_time IS '更新时间';
COMMENT ON COLUMN mm_physics.updated_by IS '更新人';

-- changeset SYS:20210820-1
DROP TABLE IF EXISTS mm_physics;
create table mm_physics
(
    physics_id varchar(36) not null constraint mm_physics_pkey primary key,
    physics_name varchar(255),
    table_id varchar(36) not null,
    datasource_id varchar(36) not null,
    created_time timestamp default now() not null,
    created_by varchar(255),
    updated_time timestamp,
    updated_by varchar(255) not null
);

comment on table mm_physics is '数据模型管理-物理表';
comment on column mm_physics.physics_id is '主键';
comment on column mm_physics.physics_name is '物理表名';
comment on column mm_physics.table_id is '逻辑表id';
comment on column mm_physics.datasource_id is '数据源id';
comment on column mm_physics.created_time is '创建时间';
comment on column mm_physics.created_by is '创建人';
comment on column mm_physics.updated_time is '更新时间';
comment on column mm_physics.updated_by is '更新人';

alter table mm_physics owner to admin;

