-- liquibase formatted sql logicalFilePath:ddl/webgis_button

-- changeset SYS:20210406-0
CREATE TABLE "webgis_button" ("button_id" VARCHAR(36) NOT NULL, "name" VARCHAR(255) NOT NULL, "type" VARCHAR(3), "method" VARCHAR(255) NOT NULL, "icon" VARCHAR(255), "description" VARCHAR(500), "status" BOOLEAN, "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), CONSTRAINT "webgis_button_pkey" PRIMARY KEY ("button_id"));
COMMENT ON TABLE "webgis_button" IS '地图按钮';
COMMENT ON COLUMN "webgis_button"."button_id" IS '按钮id。主键,uuid';
COMMENT ON COLUMN "webgis_button"."name" IS '名称。名称,title: 鼠标移入时提示信息';
COMMENT ON COLUMN "webgis_button"."type" IS '类别。1:基础类,2:业务类';
COMMENT ON COLUMN "webgis_button"."method" IS '方法。name: 要执行的方法或者要加载的组件';
COMMENT ON COLUMN "webgis_button"."icon" IS '图标。';
COMMENT ON COLUMN "webgis_button"."description" IS '描述。';
COMMENT ON COLUMN "webgis_button"."status" IS '状态。true:有效，false:停用';
COMMENT ON COLUMN "webgis_button"."created_time" IS '创建时间。生成时间不可变更';
COMMENT ON COLUMN "webgis_button"."created_by" IS '创建人。创建人user_id';
COMMENT ON COLUMN "webgis_button"."updated_time" IS '修改时间。修改时更新';
COMMENT ON COLUMN "webgis_button"."updated_by" IS '修改人。修改人user_id';
