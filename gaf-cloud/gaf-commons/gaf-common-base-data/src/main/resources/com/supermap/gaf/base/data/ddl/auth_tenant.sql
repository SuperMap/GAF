-- liquibase formatted sql logicalFilePath:ddl/auth_tenant

-- changeset SYS:20210406-0
CREATE TABLE "auth_tenant" ("tenant_id" VARCHAR(36) NOT NULL, "tenant_name" VARCHAR(255) NOT NULL, "description" VARCHAR(500), "type" VARCHAR(3), "name_en" VARCHAR(255), "brief_name_en" VARCHAR(255) NOT NULL, "code" VARCHAR(100), "admin_id" VARCHAR(36), "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), "status" BOOLEAN DEFAULT TRUE, CONSTRAINT "auth_tenant_pkey" PRIMARY KEY ("tenant_id"));
COMMENT ON TABLE "auth_tenant" IS '租户表';
COMMENT ON COLUMN "auth_tenant"."tenant_id" IS '租户id。主键,uuid';
COMMENT ON COLUMN "auth_tenant"."tenant_name" IS '租户名称。中文名称';
COMMENT ON COLUMN "auth_tenant"."description" IS '租户描述。';
COMMENT ON COLUMN "auth_tenant"."type" IS '租户类别。1:开发并运营类,2:运营类';
COMMENT ON COLUMN "auth_tenant"."name_en" IS '英文名称。';
COMMENT ON COLUMN "auth_tenant"."brief_name_en" IS '英文简称。仅允许英文字母和数字';
COMMENT ON COLUMN "auth_tenant"."code" IS '编码。4位数字';
COMMENT ON COLUMN "auth_tenant"."admin_id" IS '初始管理员id。租户初始管理员';
COMMENT ON COLUMN "auth_tenant"."created_time" IS '创建时间。';
COMMENT ON COLUMN "auth_tenant"."created_by" IS '创建人。';
COMMENT ON COLUMN "auth_tenant"."updated_time" IS '修改时间。';
COMMENT ON COLUMN "auth_tenant"."updated_by" IS '修改人。';
COMMENT ON COLUMN "auth_tenant"."status" IS '状态。true:有效，false:停用';