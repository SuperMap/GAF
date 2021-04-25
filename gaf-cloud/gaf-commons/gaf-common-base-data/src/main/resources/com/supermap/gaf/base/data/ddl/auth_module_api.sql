-- liquibase formatted sql logicalFilePath:ddl/auth_module_api

-- changeset SYS:20210406-0
CREATE TABLE "auth_module_api" ("module_api_id" VARCHAR(36) NOT NULL, "resource_module_id" VARCHAR(36) NOT NULL, "resource_api_id" VARCHAR(36) NOT NULL, "status" BOOLEAN DEFAULT TRUE, "sort_sn" INTEGER, "description" VARCHAR(500), "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), CONSTRAINT "auth_module_api_pkey" PRIMARY KEY ("module_api_id"));
COMMENT ON TABLE "auth_module_api" IS '模块接口表';
COMMENT ON COLUMN "auth_module_api"."module_api_id" IS '模块接口id。主键,uuid';
COMMENT ON COLUMN "auth_module_api"."resource_module_id" IS '模块id。';
COMMENT ON COLUMN "auth_module_api"."resource_api_id" IS '接口资源id。';
COMMENT ON COLUMN "auth_module_api"."status" IS '状态。true:有效，false:无效';
COMMENT ON COLUMN "auth_module_api"."sort_sn" IS '排序序号。同级中的序号';
COMMENT ON COLUMN "auth_module_api"."description" IS '描述。';
COMMENT ON COLUMN "auth_module_api"."created_time" IS '创建时间。生成时间不可变更';
COMMENT ON COLUMN "auth_module_api"."created_by" IS '创建人。创建人user_id';
COMMENT ON COLUMN "auth_module_api"."updated_time" IS '修改时间。修改时更新';
COMMENT ON COLUMN "auth_module_api"."updated_by" IS '修改人。修改人user_id';
