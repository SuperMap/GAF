-- liquibase formatted sql logicalFilePath:ddl/auth_department

-- changeset SYS:20210406-0
CREATE TABLE "auth_department" ("department_id" VARCHAR(36) NOT NULL, "tenant_id" VARCHAR(36) NOT NULL, "parent_id" VARCHAR(36), "sort_sn" INTEGER, "department_type" VARCHAR, "department_name" VARCHAR(255) NOT NULL, "name_en" VARCHAR(255), "brief_name" VARCHAR(255), "code" VARCHAR(100), "status" BOOLEAN DEFAULT TRUE, "description" VARCHAR(500), "admin_id" VARCHAR(36), "is_third_party" BOOLEAN, "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), CONSTRAINT "auth_department_pkey" PRIMARY KEY ("department_id"));
COMMENT ON TABLE "auth_department" IS '部门表';
COMMENT ON COLUMN "auth_department"."department_id" IS '部门id。主键,uuid';
COMMENT ON COLUMN "auth_department"."tenant_id" IS '租户id。';
COMMENT ON COLUMN "auth_department"."parent_id" IS '父级id。';
COMMENT ON COLUMN "auth_department"."sort_sn" IS '排序序号。同级中的序号';
COMMENT ON COLUMN "auth_department"."department_type" IS '部门类型。存字典code,定义：一级管理、二级研发…';
COMMENT ON COLUMN "auth_department"."department_name" IS '名称。中文名称';
COMMENT ON COLUMN "auth_department"."name_en" IS '英文名称。';
COMMENT ON COLUMN "auth_department"."brief_name" IS '简称。';
COMMENT ON COLUMN "auth_department"."code" IS '编码。每级4位数字，同级递增';
COMMENT ON COLUMN "auth_department"."status" IS '状态。true:有效，false:过期';
COMMENT ON COLUMN "auth_department"."description" IS '描述。';
COMMENT ON COLUMN "auth_department"."admin_id" IS '管理员id。部门管理员（上级管理员指定，此字段暂不使用）';
COMMENT ON COLUMN "auth_department"."is_third_party" IS '是否第三方。父级为true则子部门继承父部门该值';
COMMENT ON COLUMN "auth_department"."created_time" IS '创建时间。生成时间不可变更';
COMMENT ON COLUMN "auth_department"."created_by" IS '创建人。创建人user_id';
COMMENT ON COLUMN "auth_department"."updated_time" IS '修改时间。修改时更新';
COMMENT ON COLUMN "auth_department"."updated_by" IS '修改人。修改人user_id';