-- liquibase formatted sql logicalFilePath:ddl/auth_p3_mapping_rule

-- changeset SYS:20210406-0
CREATE TABLE "auth_p3_mapping_rule" ("mapping_rule_id" VARCHAR(36) NOT NULL, "p3_component_id" VARCHAR(36) NOT NULL, "mapping_type" VARCHAR(3) NOT NULL, "mapping_method" VARCHAR(3) NOT NULL, "mapping_impl" VARCHAR(200), "status" BOOLEAN, "description" VARCHAR(500), "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), "extra_param_json" VARCHAR(500), "mapping_rule_name" VARCHAR(255) NOT NULL, CONSTRAINT "auth_p3_mapping_rule_pkey" PRIMARY KEY ("mapping_rule_id"));
COMMENT ON TABLE "auth_p3_mapping_rule" IS '第三方映射规则表';
COMMENT ON COLUMN "auth_p3_mapping_rule"."mapping_rule_id" IS '映射规则id。主键,uuid';
COMMENT ON COLUMN "auth_p3_mapping_rule"."p3_component_id" IS '第三方组件。组件id';
COMMENT ON COLUMN "auth_p3_mapping_rule"."mapping_type" IS '映射类型。1:租户，2：部门，3：用户';
COMMENT ON COLUMN "auth_p3_mapping_rule"."mapping_method" IS '映射方式。1:推push,2:拉pull';
COMMENT ON COLUMN "auth_p3_mapping_rule"."mapping_impl" IS '映射实现类。包路径.类名。统一反射方式实现，根据映射方式调用实现类的pull或push方法';
COMMENT ON COLUMN "auth_p3_mapping_rule"."status" IS '状态。true:有效，false:无效';
COMMENT ON COLUMN "auth_p3_mapping_rule"."description" IS '描述。';
COMMENT ON COLUMN "auth_p3_mapping_rule"."created_time" IS '创建时间。生成时间不可变更';
COMMENT ON COLUMN "auth_p3_mapping_rule"."created_by" IS '创建人。创建人user_id';
COMMENT ON COLUMN "auth_p3_mapping_rule"."updated_time" IS '修改时间。修改时更新';
COMMENT ON COLUMN "auth_p3_mapping_rule"."updated_by" IS '修改人。修改人user_id';
COMMENT ON COLUMN "auth_p3_mapping_rule"."extra_param_json" IS '其他参数，不同实现方法中可能使用，json串';
COMMENT ON COLUMN "auth_p3_mapping_rule"."mapping_rule_name" IS '规则名称';