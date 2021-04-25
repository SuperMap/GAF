-- liquibase formatted sql logicalFilePath:ddl/auth_resource_module

-- changeset SYS:20210406-0
CREATE TABLE "auth_resource_module" ("resource_module_id" VARCHAR(36) NOT NULL, "sys_component_id" VARCHAR(36) NOT NULL, "module_catalog_id" VARCHAR(36) NOT NULL, "name" VARCHAR(255) NOT NULL, "type" VARCHAR(3), "module_url" VARCHAR(500), "icon_url" VARCHAR(500), "status" BOOLEAN DEFAULT TRUE, "sort_sn" INTEGER, "description" VARCHAR(500), "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), "path" VARCHAR(255), "target" VARCHAR(3) DEFAULT '0', "name_en" VARCHAR(255), CONSTRAINT "auth_resource_module_pkey" PRIMARY KEY ("resource_module_id"));
COMMENT ON TABLE "auth_resource_module" IS '模块资源表';
COMMENT ON COLUMN "auth_resource_module"."resource_module_id" IS '接口资源id。主键,uuid';
COMMENT ON COLUMN "auth_resource_module"."sys_component_id" IS '所属组件id。';
COMMENT ON COLUMN "auth_resource_module"."module_catalog_id" IS 'Module组id。';
COMMENT ON COLUMN "auth_resource_module"."name" IS '名称。中文名称';
COMMENT ON COLUMN "auth_resource_module"."type" IS '类型。1:内部页面，2：外部页面';
COMMENT ON COLUMN "auth_resource_module"."module_url" IS '地址。';
COMMENT ON COLUMN "auth_resource_module"."icon_url" IS '图标地址。';
COMMENT ON COLUMN "auth_resource_module"."status" IS '状态。true:有效，false:无效';
COMMENT ON COLUMN "auth_resource_module"."sort_sn" IS '排序序号。同级中的序号';
COMMENT ON COLUMN "auth_resource_module"."description" IS '描述。';
COMMENT ON COLUMN "auth_resource_module"."created_time" IS '创建时间。生成时间不可变更';
COMMENT ON COLUMN "auth_resource_module"."created_by" IS '创建人。创建人user_id';
COMMENT ON COLUMN "auth_resource_module"."updated_time" IS '修改时间。修改时更新';
COMMENT ON COLUMN "auth_resource_module"."updated_by" IS '修改人。修改人user_id';
COMMENT ON COLUMN "auth_resource_module"."path" IS '前端路由路径。';
COMMENT ON COLUMN "auth_resource_module"."target" IS '打开方式。0:当前窗口(或当前窗口新开tab),1:新窗口打开';
COMMENT ON COLUMN "auth_resource_module"."name_en" IS '英文名称。';
