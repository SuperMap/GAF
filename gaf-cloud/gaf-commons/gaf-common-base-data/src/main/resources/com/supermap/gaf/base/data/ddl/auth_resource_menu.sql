-- liquibase formatted sql logicalFilePath:ddl/auth_resource_menu

-- changeset SYS:20210406-0
CREATE TABLE "auth_resource_menu" ("resource_menu_id" VARCHAR(36) NOT NULL, "menu_catalog_id" VARCHAR(36) NOT NULL, "name" VARCHAR(255) NOT NULL, "resource_module_id" VARCHAR(36) NOT NULL, "icon" VARCHAR(255), "visible" BOOLEAN, "status" BOOLEAN, "sort_sn" INTEGER, "description" VARCHAR(500), "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), "params" VARCHAR(500), CONSTRAINT "auth_resource_menu_pkey" PRIMARY KEY ("resource_menu_id"));
COMMENT ON TABLE "auth_resource_menu" IS '菜单表';
COMMENT ON COLUMN "auth_resource_menu"."resource_menu_id" IS '菜单id。主键,uuid';
COMMENT ON COLUMN "auth_resource_menu"."menu_catalog_id" IS '菜单组。菜单组id';
COMMENT ON COLUMN "auth_resource_menu"."name" IS '显示名称。显示名称。中文名称，默认与模块名称相同';
COMMENT ON COLUMN "auth_resource_menu"."resource_module_id" IS '关联模块。模块id';
COMMENT ON COLUMN "auth_resource_menu"."icon" IS '图标。';
COMMENT ON COLUMN "auth_resource_menu"."visible" IS '是否可见。true:可见，false:不可见';
COMMENT ON COLUMN "auth_resource_menu"."status" IS '状态。true:有效，false:无效';
COMMENT ON COLUMN "auth_resource_menu"."sort_sn" IS '排序序号。同级中的序号';
COMMENT ON COLUMN "auth_resource_menu"."description" IS '描述。';
COMMENT ON COLUMN "auth_resource_menu"."created_time" IS '创建时间。生成时间不可变更';
COMMENT ON COLUMN "auth_resource_menu"."created_by" IS '创建人。创建人user_id';
COMMENT ON COLUMN "auth_resource_menu"."updated_time" IS '修改时间。修改时更新';
COMMENT ON COLUMN "auth_resource_menu"."updated_by" IS '修改人。修改人user_id';
COMMENT ON COLUMN "auth_resource_menu"."params" IS '参数。入口参数和值,para1=value1&para2=value2。菜单实际路径为模块路径加参数(url?params)';
