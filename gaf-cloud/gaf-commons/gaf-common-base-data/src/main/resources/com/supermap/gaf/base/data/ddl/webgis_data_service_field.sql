-- liquibase formatted sql logicalFilePath:ddl/webgis_data_service_field

-- changeset SYS:20210406-0
CREATE TABLE "webgis_data_service_field" ("gis_service_field_id" VARCHAR(36) NOT NULL, "gis_data_service_id" VARCHAR(36) NOT NULL, "field_name" VARCHAR(50), "field_alias" VARCHAR(50), "field_type_code" VARCHAR(50), "description" VARCHAR(500), "status" BOOLEAN, "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), CONSTRAINT "webgis_data_service_field_pkey" PRIMARY KEY ("gis_service_field_id"));
COMMENT ON TABLE "webgis_data_service_field" IS 'GIS数据服务字段表';
COMMENT ON COLUMN "webgis_data_service_field"."gis_service_field_id" IS 'GIS服务字段id。主键,uuid';
COMMENT ON COLUMN "webgis_data_service_field"."gis_data_service_id" IS 'GIS数据服务。数据服务的服务id,webgis_service中的一个数据服务';
COMMENT ON COLUMN "webgis_data_service_field"."field_name" IS '字段名称。';
COMMENT ON COLUMN "webgis_data_service_field"."field_alias" IS '字段别名。';
COMMENT ON COLUMN "webgis_data_service_field"."field_type_code" IS '字段类型。字段类型编码';
COMMENT ON COLUMN "webgis_data_service_field"."description" IS '描述。';
COMMENT ON COLUMN "webgis_data_service_field"."status" IS '状态。true:有效，false:停用';
COMMENT ON COLUMN "webgis_data_service_field"."created_time" IS '创建时间。生成时间不可变更';
COMMENT ON COLUMN "webgis_data_service_field"."created_by" IS '创建人。创建人user_id';
COMMENT ON COLUMN "webgis_data_service_field"."updated_time" IS '修改时间。修改时更新';
COMMENT ON COLUMN "webgis_data_service_field"."updated_by" IS '修改人。修改人user_id';