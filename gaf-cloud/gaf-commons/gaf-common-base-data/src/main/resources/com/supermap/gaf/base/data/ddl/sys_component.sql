-- liquibase formatted sql logicalFilePath:ddl/sys_component

-- changeset SYS:20210406-0
CREATE TABLE "sys_component" ("sys_component_id" VARCHAR(36) NOT NULL, "name" VARCHAR(255) NOT NULL, "name_cn" VARCHAR(255) NOT NULL, "code" VARCHAR(100), "type" VARCHAR(3), "status" BOOLEAN DEFAULT TRUE, "description" VARCHAR(500), "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), "sort_sn" INTEGER, CONSTRAINT "sys_component_pkey" PRIMARY KEY ("sys_component_id"));
COMMENT ON TABLE "sys_component" IS '系统组件表';
COMMENT ON COLUMN "sys_component"."sys_component_id" IS '系统组件id。主键,uuid';
COMMENT ON COLUMN "sys_component"."name" IS '名称。英文名称';
COMMENT ON COLUMN "sys_component"."name_cn" IS '中文名称。';
COMMENT ON COLUMN "sys_component"."code" IS '编码。应用编码+4位数字';
COMMENT ON COLUMN "sys_component"."type" IS '类型。1：web前端，2：后端，3：移动端，4：web前后端';
COMMENT ON COLUMN "sys_component"."status" IS '可见性。有效性.true:有效/可见，false:无效/不可见';
COMMENT ON COLUMN "sys_component"."description" IS '描述。';
COMMENT ON COLUMN "sys_component"."created_time" IS '创建时间。';
COMMENT ON COLUMN "sys_component"."created_by" IS '创建人。';
COMMENT ON COLUMN "sys_component"."updated_time" IS '修改时间。';
COMMENT ON COLUMN "sys_component"."updated_by" IS '修改人。';
COMMENT ON COLUMN "sys_component"."sort_sn" IS '排序序号。';