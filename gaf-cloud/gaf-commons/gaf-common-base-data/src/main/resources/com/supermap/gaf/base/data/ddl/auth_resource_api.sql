-- liquibase formatted sql logicalFilePath:ddl/auth_resource_api

-- changeset SYS:20210406-0
CREATE TABLE "auth_resource_api" ("resource_api_id" VARCHAR(36) NOT NULL, "sys_component_id" VARCHAR(36) NOT NULL, "api_catalog_id" VARCHAR(36) NOT NULL, "name" VARCHAR(255) NOT NULL, "route_url" VARCHAR(500), "method" VARCHAR(2), "type" VARCHAR(3), "status" BOOLEAN DEFAULT TRUE, "sort_sn" INTEGER, "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), CONSTRAINT "auth_resource_api_pkey" PRIMARY KEY ("resource_api_id"));
COMMENT ON TABLE "auth_resource_api" IS 'API资源表';
COMMENT ON COLUMN "auth_resource_api"."resource_api_id" IS '接口资源id。主键,uuid';
COMMENT ON COLUMN "auth_resource_api"."sys_component_id" IS '所属组件id。';
COMMENT ON COLUMN "auth_resource_api"."api_catalog_id" IS 'API组id。';
COMMENT ON COLUMN "auth_resource_api"."name" IS '名称。中文名称';
COMMENT ON COLUMN "auth_resource_api"."route_url" IS '路由路径。';
COMMENT ON COLUMN "auth_resource_api"."method" IS '方法。1:GET,2:POST,3:PUT,4:DELETE,…';
COMMENT ON COLUMN "auth_resource_api"."type" IS '类型。1:应用组件资源，2：第三方资源';
COMMENT ON COLUMN "auth_resource_api"."status" IS '状态。true:有效，false:无效';
COMMENT ON COLUMN "auth_resource_api"."sort_sn" IS '排序序号。同级中的序号';
COMMENT ON COLUMN "auth_resource_api"."created_time" IS '创建时间。生成时间不可变更';
COMMENT ON COLUMN "auth_resource_api"."created_by" IS '创建人。创建人user_id';
COMMENT ON COLUMN "auth_resource_api"."updated_time" IS '修改时间。修改时更新';
COMMENT ON COLUMN "auth_resource_api"."updated_by" IS '修改人。修改人user_id';