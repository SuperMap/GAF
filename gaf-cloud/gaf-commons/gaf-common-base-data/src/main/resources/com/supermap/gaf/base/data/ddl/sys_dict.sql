-- liquibase formatted sql logicalFilePath:ddl/sys_dict

-- changeset SYS:20210406-0
CREATE TABLE "sys_dict" ("data_dict_id" VARCHAR(36) NOT NULL, "dict_code" VARCHAR(30), "dict_name" VARCHAR(30) NOT NULL, "dict_value" VARCHAR(50), "dict_class" VARCHAR(10), "seq" INTEGER, "pid" VARCHAR(36), "dict_desc" VARCHAR(255), "status" BOOLEAN DEFAULT TRUE, "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(36), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(36), "tenant_id" VARCHAR(36), "visibility" BOOLEAN DEFAULT TRUE, "catalog_id" VARCHAR(36), "ext_properties" VARCHAR(500), CONSTRAINT "sys_dict_pkey" PRIMARY KEY ("data_dict_id"));
COMMENT ON TABLE "sys_dict" IS '系统数据字典表';
COMMENT ON COLUMN "sys_dict"."data_dict_id" IS '字典id。主键,uuid';
COMMENT ON COLUMN "sys_dict"."dict_code" IS '编码。分类编码';
COMMENT ON COLUMN "sys_dict"."dict_name" IS '名称。数据项显示名label';
COMMENT ON COLUMN "sys_dict"."dict_value" IS '值。数据项value';
COMMENT ON COLUMN "sys_dict"."dict_class" IS '类别。1:普通字典，2：级联字典';
COMMENT ON COLUMN "sys_dict"."seq" IS '类别中序号。';
COMMENT ON COLUMN "sys_dict"."pid" IS '所属类型。父级id，0则为字典类型，非0为字典数据';
COMMENT ON COLUMN "sys_dict"."dict_desc" IS '描述。';
COMMENT ON COLUMN "sys_dict"."status" IS '状态。0:无效，1：有效';
COMMENT ON COLUMN "sys_dict"."created_time" IS '创建时间。';
COMMENT ON COLUMN "sys_dict"."created_by" IS '创建人。';
COMMENT ON COLUMN "sys_dict"."updated_time" IS '修改时间。';
COMMENT ON COLUMN "sys_dict"."updated_by" IS '修改人。';
COMMENT ON COLUMN "sys_dict"."tenant_id" IS '租户id';
COMMENT ON COLUMN "sys_dict"."visibility" IS '可见性。true:可见，false:不可见';
COMMENT ON COLUMN "sys_dict"."catalog_id" IS '字典目录。关联sys_catalog表';
COMMENT ON COLUMN "sys_dict"."ext_properties" IS '扩展属性。自定义属性，json格式';