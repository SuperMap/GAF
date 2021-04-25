-- liquibase formatted sql logicalFilePath:ddl/auth_role

-- changeset SYS:20210406-0
CREATE TABLE "auth_role" ("role_id" VARCHAR(36) NOT NULL, "tenant_id" VARCHAR(36) NOT NULL, "role_catalog_id" VARCHAR(36) NOT NULL, "sort_sn" INTEGER, "role_name" VARCHAR(255) NOT NULL, "name_en" VARCHAR(255), "code" VARCHAR(100), "status" BOOLEAN DEFAULT TRUE, "description" VARCHAR(500), "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), "type" VARCHAR(3), CONSTRAINT "auth_role_pkey" PRIMARY KEY ("role_id"));
COMMENT ON TABLE "auth_role" IS '角色表';
COMMENT ON COLUMN "auth_role"."role_id" IS '角色id。主键,uuid';
COMMENT ON COLUMN "auth_role"."tenant_id" IS '租户id。所属租户,组件关联的角色组/角色此值设为000000';
COMMENT ON COLUMN "auth_role"."role_catalog_id" IS '角色组id。关联角色组目录';
COMMENT ON COLUMN "auth_role"."sort_sn" IS '排序序号。同级中的序号';
COMMENT ON COLUMN "auth_role"."role_name" IS '名称。中文名称';
COMMENT ON COLUMN "auth_role"."name_en" IS '英文名称。';
COMMENT ON COLUMN "auth_role"."code" IS '编码。角色组编码+4位编码';
COMMENT ON COLUMN "auth_role"."status" IS '状态。true:有效，false:过期';
COMMENT ON COLUMN "auth_role"."description" IS '描述。';
COMMENT ON COLUMN "auth_role"."created_time" IS '创建时间。生成时间不可变更';
COMMENT ON COLUMN "auth_role"."created_by" IS '创建人。创建人user_id';
COMMENT ON COLUMN "auth_role"."updated_time" IS '修改时间。修改时更新';
COMMENT ON COLUMN "auth_role"."updated_by" IS '修改人。修改人user_id';
COMMENT ON COLUMN "auth_role"."type" IS '类别。1:组件内置（租户可见），2：平台级（租户不可见），3：租户级';