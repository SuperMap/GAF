-- liquibase formatted sql logicalFilePath:ddl/webgis_catalog_layer

-- changeset SYS:20210406-0
CREATE TABLE "webgis_catalog_layer" ("catalog_layer_id" VARCHAR(36) NOT NULL, "name" VARCHAR(255) NOT NULL, "layer_catalog_id" VARCHAR(36), "gis_service_id" VARCHAR(36), "sort_sn" INTEGER, "init_load" BOOLEAN, "description" VARCHAR(500), "status" BOOLEAN, "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), CONSTRAINT "webgis_catalog_layer_pkey" PRIMARY KEY ("catalog_layer_id"));
COMMENT ON TABLE "webgis_catalog_layer" IS '资源目录图层';
COMMENT ON COLUMN "webgis_catalog_layer"."catalog_layer_id" IS '图层目录id。主键,uuid';
COMMENT ON COLUMN "webgis_catalog_layer"."name" IS '名称。中文名称';
COMMENT ON COLUMN "webgis_catalog_layer"."layer_catalog_id" IS '所属目录。';
COMMENT ON COLUMN "webgis_catalog_layer"."gis_service_id" IS 'GIS服务。';
COMMENT ON COLUMN "webgis_catalog_layer"."sort_sn" IS '排序序号。同级中的序号';
COMMENT ON COLUMN "webgis_catalog_layer"."init_load" IS '初始加载。true:是，false:否';
COMMENT ON COLUMN "webgis_catalog_layer"."description" IS '描述。';
COMMENT ON COLUMN "webgis_catalog_layer"."status" IS '状态。true:有效，false:停用';
COMMENT ON COLUMN "webgis_catalog_layer"."created_time" IS '创建时间。生成时间不可变更';
COMMENT ON COLUMN "webgis_catalog_layer"."created_by" IS '创建人。创建人user_id';
COMMENT ON COLUMN "webgis_catalog_layer"."updated_time" IS '修改时间。修改时更新';
COMMENT ON COLUMN "webgis_catalog_layer"."updated_by" IS '修改人。修改人user_id';