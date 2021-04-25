-- liquibase formatted sql logicalFilePath:ddl/webgis_roam_route

-- changeset SYS:20210406-0
CREATE TABLE "webgis_roam_route" ("gis_roam_route_id" VARCHAR(36) NOT NULL, "gis_app_id" VARCHAR(36), "user_id" VARCHAR(36), "name" VARCHAR(50) NOT NULL, "speed" numeric, "fpf_path" VARCHAR(500), "description" VARCHAR(500), "status" BOOLEAN, "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(50), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(50), CONSTRAINT "webgis_roam_route_pkey" PRIMARY KEY ("gis_roam_route_id"));
COMMENT ON TABLE "webgis_roam_route" IS '漫游路线表';
COMMENT ON COLUMN "webgis_roam_route"."gis_roam_route_id" IS '漫游路线id。主键,uuid';
COMMENT ON COLUMN "webgis_roam_route"."gis_app_id" IS '地图应用。所属应用';
COMMENT ON COLUMN "webgis_roam_route"."user_id" IS '用户。所属用户（创建时created_by/修改是updated_by）';
COMMENT ON COLUMN "webgis_roam_route"."name" IS '名称。同应用或同用户下不同名。漫游路线要生成对应的文件，文件名为gis_roam_route_id.fpf';
COMMENT ON COLUMN "webgis_roam_route"."speed" IS '速度。';
COMMENT ON COLUMN "webgis_roam_route"."fpf_path" IS '模板文件路径。';
COMMENT ON COLUMN "webgis_roam_route"."description" IS '描述。';
COMMENT ON COLUMN "webgis_roam_route"."status" IS '状态。true:有效，false:停用';
COMMENT ON COLUMN "webgis_roam_route"."created_time" IS '创建时间。生成时间不可变更';
COMMENT ON COLUMN "webgis_roam_route"."created_by" IS '创建人。创建人user_id';
COMMENT ON COLUMN "webgis_roam_route"."updated_time" IS '修改时间。修改时更新';
COMMENT ON COLUMN "webgis_roam_route"."updated_by" IS '修改人。修改人user_id';