-- liquibase formatted sql logicalFilePath:ddl/auth_p3_department_mapping

-- changeset SYS:20210406-0
CREATE TABLE "auth_p3_department_mapping" ("department_mapping_id" VARCHAR(36) NOT NULL, "department_id" VARCHAR(36) NOT NULL, "p3_department_id" VARCHAR(100) NOT NULL, "p3_component_id" VARCHAR(36) NOT NULL, "status" BOOLEAN, "description" VARCHAR(500), "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), "p3_tenant_id" VARCHAR(100), CONSTRAINT "auth_p3_department_mapping_pkey" PRIMARY KEY ("department_mapping_id"));
COMMENT ON TABLE "auth_p3_department_mapping" IS '第三方部门映射表';
COMMENT ON COLUMN "auth_p3_department_mapping"."department_mapping_id" IS '部门映射id。主键,uuid';
COMMENT ON COLUMN "auth_p3_department_mapping"."department_id" IS '部门。GAF部门id';
COMMENT ON COLUMN "auth_p3_department_mapping"."p3_department_id" IS '第三方部门。';
COMMENT ON COLUMN "auth_p3_department_mapping"."p3_component_id" IS '第三方组件。组件id';
COMMENT ON COLUMN "auth_p3_department_mapping"."status" IS '状态。true:有效，false:无效';
COMMENT ON COLUMN "auth_p3_department_mapping"."description" IS '描述。';
COMMENT ON COLUMN "auth_p3_department_mapping"."created_time" IS '创建时间。生成时间不可变更';
COMMENT ON COLUMN "auth_p3_department_mapping"."created_by" IS '创建人。创建人user_id';
COMMENT ON COLUMN "auth_p3_department_mapping"."updated_time" IS '修改时间。修改时更新';
COMMENT ON COLUMN "auth_p3_department_mapping"."updated_by" IS '修改人。修改人user_id';
COMMENT ON COLUMN "auth_p3_department_mapping"."p3_tenant_id" IS '所属第三方租户。';