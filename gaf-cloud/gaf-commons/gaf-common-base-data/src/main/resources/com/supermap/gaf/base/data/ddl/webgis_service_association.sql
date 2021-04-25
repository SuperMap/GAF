-- liquibase formatted sql logicalFilePath:ddl/webgis_service_association

-- changeset SYS:20210406-0
CREATE TABLE "webgis_service_association" ("gis_service_assoc_id" VARCHAR(36) NOT NULL, "gis_service_id" VARCHAR(36) NOT NULL, "gis_data_service_id" VARCHAR(36) NOT NULL, "description" VARCHAR(500), "status" BOOLEAN, "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), CONSTRAINT "webgis_service_association_pkey" PRIMARY KEY ("gis_service_assoc_id"));
COMMENT ON TABLE "webgis_service_association" IS 'GIS服务关联表';
COMMENT ON COLUMN "webgis_service_association"."gis_service_assoc_id" IS 'GIS服务关联id。主键,uuid';
COMMENT ON COLUMN "webgis_service_association"."gis_service_id" IS 'GIS服务。服务id，一个地图服务，对应一外或多个数据服务';
COMMENT ON COLUMN "webgis_service_association"."gis_data_service_id" IS 'GIS数据服务id。数据服务的服务,webgis_service中的一个数据服务';
COMMENT ON COLUMN "webgis_service_association"."description" IS '描述。';
COMMENT ON COLUMN "webgis_service_association"."status" IS '状态。true:有效，false:停用';
COMMENT ON COLUMN "webgis_service_association"."created_time" IS '创建时间。生成时间不可变更';
COMMENT ON COLUMN "webgis_service_association"."created_by" IS '创建人。创建人user_id';
COMMENT ON COLUMN "webgis_service_association"."updated_time" IS '修改时间。修改时更新';
COMMENT ON COLUMN "webgis_service_association"."updated_by" IS '修改人。修改人user_id';