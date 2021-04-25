-- liquibase formatted sql logicalFilePath:ddl/webgis_roam_stop

-- changeset SYS:20210406-0
CREATE TABLE "webgis_roam_stop" ("gis_roam_stop_id" VARCHAR(36) NOT NULL, "name" VARCHAR NOT NULL, "gis_roam_route_id" VARCHAR(36) NOT NULL, "speed" numeric, "usemyspeed" BOOLEAN DEFAULT FALSE, "longitude" numeric, "latitude" numeric, "altitude" numeric, "altitudemode" VARCHAR, "heading" numeric, "tilt" numeric, "sight_center_x" numeric, "sight_center_y" numeric, "sight_center_z" numeric, "sort_sn" INTEGER, "description" VARCHAR(500), "status" BOOLEAN, "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(50), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(50), CONSTRAINT "webgis_roam_stop_pkey" PRIMARY KEY ("gis_roam_stop_id"));
COMMENT ON TABLE "webgis_roam_stop" IS '漫游站点表';
COMMENT ON COLUMN "webgis_roam_stop"."gis_roam_stop_id" IS '漫游点id。主键,uuid';
COMMENT ON COLUMN "webgis_roam_stop"."name" IS '站点名称。';
COMMENT ON COLUMN "webgis_roam_stop"."gis_roam_route_id" IS '漫游路线。';
COMMENT ON COLUMN "webgis_roam_stop"."speed" IS '站点速度。';
COMMENT ON COLUMN "webgis_roam_stop"."usemyspeed" IS '是否使用站点速度。true:使用，false:不用';
COMMENT ON COLUMN "webgis_roam_stop"."longitude" IS '相机经度。';
COMMENT ON COLUMN "webgis_roam_stop"."latitude" IS '相机纬度。';
COMMENT ON COLUMN "webgis_roam_stop"."altitude" IS '相机高度。';
COMMENT ON COLUMN "webgis_roam_stop"."altitudemode" IS '高度模式。ABSOLUTE：绝对高度,ClampToGround:';
COMMENT ON COLUMN "webgis_roam_stop"."heading" IS '相机方位角。';
COMMENT ON COLUMN "webgis_roam_stop"."tilt" IS '相机俯仰角。';
COMMENT ON COLUMN "webgis_roam_stop"."sight_center_x" IS '视线中心点位置x。json';
COMMENT ON COLUMN "webgis_roam_stop"."sight_center_y" IS '视线中心点位置y。json';
COMMENT ON COLUMN "webgis_roam_stop"."sight_center_z" IS '视线中心点位置z。json';
COMMENT ON COLUMN "webgis_roam_stop"."sort_sn" IS '排序序号。序号从1开始';
COMMENT ON COLUMN "webgis_roam_stop"."description" IS '描述。';
COMMENT ON COLUMN "webgis_roam_stop"."status" IS '状态。true:有效，false:停用';
COMMENT ON COLUMN "webgis_roam_stop"."created_time" IS '创建时间。生成时间不可变更';
COMMENT ON COLUMN "webgis_roam_stop"."created_by" IS '创建人。创建人user_id';
COMMENT ON COLUMN "webgis_roam_stop"."updated_time" IS '修改时间。修改时更新';
COMMENT ON COLUMN "webgis_roam_stop"."updated_by" IS '修改人。修改人user_id';