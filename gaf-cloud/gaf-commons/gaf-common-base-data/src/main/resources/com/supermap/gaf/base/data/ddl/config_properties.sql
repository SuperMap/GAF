-- liquibase formatted sql logicalFilePath:ddl/config_properties

-- changeset SYS:20210406-0
CREATE TABLE "config_properties" ("id" VARCHAR(64) NOT NULL, "pro_key" VARCHAR(255) NOT NULL, "pro_value" VARCHAR(500) NOT NULL, "application" VARCHAR(50) NOT NULL, "profile" VARCHAR(50) NOT NULL, "label" VARCHAR(50), "tenant_id" VARCHAR(50) NOT NULL, "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), "description" VARCHAR(500), CONSTRAINT "config_properties_pkey" PRIMARY KEY ("id"));
COMMENT ON TABLE "config_properties" IS '配置中心表';
COMMENT ON COLUMN "config_properties"."created_time" IS '创建时间。生成时间不可变更';
COMMENT ON COLUMN "config_properties"."created_by" IS '创建人。创建人user_id';
COMMENT ON COLUMN "config_properties"."updated_time" IS '修改时间。修改时更新';
COMMENT ON COLUMN "config_properties"."updated_by" IS '修改人。修改人user_id';
COMMENT ON COLUMN "config_properties"."description" IS '描述。配置项说明、填写要求及示例';