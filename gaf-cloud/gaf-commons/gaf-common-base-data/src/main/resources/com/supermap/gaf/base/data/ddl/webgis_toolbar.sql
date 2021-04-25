-- liquibase formatted sql logicalFilePath:ddl/webgis_toolbar

-- changeset SYS:20210406-0
CREATE TABLE "webgis_toolbar" ("toolbar_id" VARCHAR(36) NOT NULL, "name" VARCHAR(255) NOT NULL, "toolbar_location" VARCHAR(3), "description" VARCHAR(500), "status" BOOLEAN, "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), "type" VARCHAR(3), CONSTRAINT "webgis_toolbar_pkey" PRIMARY KEY ("toolbar_id"));
COMMENT ON TABLE "webgis_toolbar" IS '地图工具条';
COMMENT ON COLUMN "webgis_toolbar"."toolbar_id" IS '工具条id。主键,uuid';
COMMENT ON COLUMN "webgis_toolbar"."name" IS '名称。';
COMMENT ON COLUMN "webgis_toolbar"."toolbar_location" IS '位置。1：顶平，2：左纵上，3：左纵上,4：底平，5：右纵上，6.右纵下（字典表？）';
COMMENT ON COLUMN "webgis_toolbar"."description" IS '描述。';
COMMENT ON COLUMN "webgis_toolbar"."status" IS '状态。true:有效，false:停用';
COMMENT ON COLUMN "webgis_toolbar"."created_time" IS '创建时间。生成时间不可变更';
COMMENT ON COLUMN "webgis_toolbar"."created_by" IS '创建人。创建人user_id';
COMMENT ON COLUMN "webgis_toolbar"."updated_time" IS '修改时间。修改时更新';
COMMENT ON COLUMN "webgis_toolbar"."updated_by" IS '修改人。修改人user_id';
COMMENT ON COLUMN "webgis_toolbar"."type" IS '类别。1:基础类,2:业务类(工具条只能选用同种类型按钮)';
