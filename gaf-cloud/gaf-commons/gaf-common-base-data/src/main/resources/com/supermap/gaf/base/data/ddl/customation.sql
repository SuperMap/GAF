-- liquibase formatted sql logicalFilePath:ddl/customation

-- changeset SYS:20210406-0
CREATE TABLE "customation" ("loginAdress" VARCHAR(255) NOT NULL, "logoutAdress" VARCHAR(255) NOT NULL, "profileAdress" VARCHAR(255) NOT NULL, "logo" VARCHAR(255) NOT NULL, "title" VARCHAR(255) NOT NULL, "color" VARCHAR(255) NOT NULL, "multipleTabs" BOOLEAN NOT NULL, "user" VARCHAR(255), "layoutType" VARCHAR(255) NOT NULL, "configInfo" TEXT, "defaultConfigInfo" TEXT, "id" VARCHAR(36) NOT NULL, "tenant_id" VARCHAR(36) NOT NULL, "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), CONSTRAINT "customation_pkey" PRIMARY KEY ("id"));
COMMENT ON TABLE "customation" IS '门户定制';
COMMENT ON COLUMN "customation"."id" IS '订制id。主键,uuid';
COMMENT ON COLUMN "customation"."tenant_id" IS '所属租户。所属租户id';
COMMENT ON COLUMN "customation"."created_time" IS '创建时间。生成时间不可变更';
COMMENT ON COLUMN "customation"."created_by" IS '创建人。创建人user_id';
COMMENT ON COLUMN "customation"."updated_time" IS '修改时间。修改时更新';
COMMENT ON COLUMN "customation"."updated_by" IS '修改人。修改人user_id';