-- liquibase formatted sql logicalFilePath:ddl/sys_catalog

-- changeset SYS:20210406-0
CREATE TABLE "sys_catalog" ("catalog_id" VARCHAR(36) NOT NULL, "parent_id" VARCHAR(36), "sort_sn" INTEGER, "name" VARCHAR(255) NOT NULL, "type" VARCHAR(3), "code" VARCHAR(100), "sys_component_id" VARCHAR(36), "icon_url" VARCHAR(500), "description" VARCHAR(500), "status" BOOLEAN DEFAULT TRUE, "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), "tenant_id" VARCHAR(36), "biz_type_code" VARCHAR(3), CONSTRAINT "sys_catalog_pkey" PRIMARY KEY ("catalog_id"));
COMMENT ON TABLE "sys_catalog" IS '分类目录表';
COMMENT ON COLUMN "sys_catalog"."catalog_id" IS '目录id。主键,uuid';
COMMENT ON COLUMN "sys_catalog"."parent_id" IS '上级目录id。(创建目录类别时，生成该类别根记录)';
COMMENT ON COLUMN "sys_catalog"."sort_sn" IS '排序序号。同级中的序号';
COMMENT ON COLUMN "sys_catalog"."name" IS '目录名称。中文名称';
COMMENT ON COLUMN "sys_catalog"."type" IS '1：组件模块（菜单），2：API分组，3：角色分组，4：菜单分组，5:webgis服务分组，6：资源目录,7:字典目录';
COMMENT ON COLUMN "sys_catalog"."code" IS '编码。每级4位数字，同级递增';
COMMENT ON COLUMN "sys_catalog"."sys_component_id" IS '所属组件。类别为1和2时使用';
COMMENT ON COLUMN "sys_catalog"."icon_url" IS '图标地址。';
COMMENT ON COLUMN "sys_catalog"."description" IS '描述。';
COMMENT ON COLUMN "sys_catalog"."status" IS '状态。true:有效，false:无效';
COMMENT ON COLUMN "sys_catalog"."created_time" IS '创建时间。生成时间不可变更';
COMMENT ON COLUMN "sys_catalog"."created_by" IS '创建人。创建人user_id';
COMMENT ON COLUMN "sys_catalog"."updated_time" IS '修改时间。修改时更新';
COMMENT ON COLUMN "sys_catalog"."updated_by" IS '修改人。修改人user_id';
COMMENT ON COLUMN "sys_catalog"."tenant_id" IS '租户id。所属租户,组件关联的角色组/角色此值设为000000';
COMMENT ON COLUMN "sys_catalog"."biz_type_code" IS '业务类别。业务类型字典码，资源目录等业务数据目录使用';