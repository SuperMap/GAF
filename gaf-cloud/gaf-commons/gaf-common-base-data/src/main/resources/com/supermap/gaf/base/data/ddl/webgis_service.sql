-- liquibase formatted sql logicalFilePath:ddl/webgis_service

-- changeset SYS:20210406-0
CREATE TABLE "webgis_service" ("gis_service_id" VARCHAR(36) NOT NULL, "name" VARCHAR(255) NOT NULL, "name_en" VARCHAR(255), "type_code" VARCHAR(30), "resource_api_id" VARCHAR(36), "address" VARCHAR(500), "time_attribute" TIMESTAMP WITHOUT TIME ZONE, "more_properties" TEXT, "description" VARCHAR(500), "status" BOOLEAN, "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), "tianditu_service_type" VARCHAR(200), "region_code" VARCHAR(100), "display_attrs" VARCHAR(500), "intf_type_code" VARCHAR(200), CONSTRAINT "webgis_service_pkey" PRIMARY KEY ("gis_service_id"));
COMMENT ON TABLE "webgis_service" IS 'GIS服务';
COMMENT ON COLUMN "webgis_service"."gis_service_id" IS 'GIS服务id。主键,uuid';
COMMENT ON COLUMN "webgis_service"."name" IS '名称。中文名称';
COMMENT ON COLUMN "webgis_service"."name_en" IS '英文名称。';
COMMENT ON COLUMN "webgis_service"."type_code" IS '类别。选自数据字典：服务类别，存各级code斜级分隔(level1code/level2code...)';
COMMENT ON COLUMN "webgis_service"."resource_api_id" IS 'API资源id。服务名称、地址从api资源表中获取(name,route_url)，资源表中建立根目录webgis子目录为此类别，资源表中类型为第三方';
COMMENT ON COLUMN "webgis_service"."address" IS '地址。resource_api_id为空则服务地址存于此字段';
COMMENT ON COLUMN "webgis_service"."time_attribute" IS '时态。年月日,yyyyMMdd';
COMMENT ON COLUMN "webgis_service"."more_properties" IS '扩展属性。自定义属性,json数据格式，接口读出转为json时平铺放到服务属性中去';
COMMENT ON COLUMN "webgis_service"."description" IS '描述。';
COMMENT ON COLUMN "webgis_service"."status" IS '状态。true:有效，false:停用';
COMMENT ON COLUMN "webgis_service"."created_time" IS '创建时间。生成时间不可变更';
COMMENT ON COLUMN "webgis_service"."created_by" IS '创建人。创建人user_id';
COMMENT ON COLUMN "webgis_service"."updated_time" IS '修改时间。修改时更新';
COMMENT ON COLUMN "webgis_service"."updated_by" IS '修改人。修改人user_id';
COMMENT ON COLUMN "webgis_service"."tianditu_service_type" IS '天地图服务类型。';
COMMENT ON COLUMN "webgis_service"."region_code" IS '行政区划。选自数据字典：行政区划，存各级code斜级分隔(level1code/level2code...)';
COMMENT ON COLUMN "webgis_service"."display_attrs" IS '显示属性。json:{[...]}二维地图服务，设置最小以及最大显示级别,[{minimumTerrainLevel:5,maximumTerrainLevel:7}];三维地图服务，设置最小以及最大显示相机高度,[{name:"现状建筑@三维转换成果"，visibleDistanceMin:0,visibleDistanceMax:2000}...]';
COMMENT ON COLUMN "webgis_service"."intf_type_code" IS '选自数据字典：服务接口类型，存各级code斜级分隔(level1code/level2code...)。可有多个接口类型编码，英文逗号分隔';
