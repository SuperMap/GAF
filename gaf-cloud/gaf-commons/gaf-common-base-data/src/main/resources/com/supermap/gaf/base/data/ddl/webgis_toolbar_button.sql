-- liquibase formatted sql logicalFilePath:ddl/webgis_toolbar_button

-- changeset SYS:20210406-0
CREATE TABLE "webgis_toolbar_button" ("toolbar_button_id" VARCHAR(36) NOT NULL, "toolbar_id" VARCHAR(36) NOT NULL, "button_id" VARCHAR(36) NOT NULL, "icon" VARCHAR(255), "params" VARCHAR(10000), "actions" VARCHAR(4000), "toggle" BOOLEAN, "close_other_panel" BOOLEAN, "more_properties" TEXT, "sort_sn" INTEGER, "description" VARCHAR(500), "status" BOOLEAN, "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), "sub_toolbar_id" VARCHAR(36), CONSTRAINT "webgis_toolbar_button_pkey" PRIMARY KEY ("toolbar_button_id"));
COMMENT ON TABLE "webgis_toolbar_button" IS '地图工具条按钮';
COMMENT ON COLUMN "webgis_toolbar_button"."toolbar_button_id" IS '工具条按钮id。主键,uuid';
COMMENT ON COLUMN "webgis_toolbar_button"."toolbar_id" IS '工具条。主键,uuid';
COMMENT ON COLUMN "webgis_toolbar_button"."button_id" IS '按钮。';
COMMENT ON COLUMN "webgis_toolbar_button"."icon" IS '图标。若不为空则覆盖按钮的icon属性';
COMMENT ON COLUMN "webgis_toolbar_button"."params" IS '参数。params: ["参数1", "参数2"], 为方法时可以传递的参数';
COMMENT ON COLUMN "webgis_toolbar_button"."actions" IS '其它方法。actions: ["func1", "func2"],当加载组件或者执行方法时要执行的其他工具类方法';
COMMENT ON COLUMN "webgis_toolbar_button"."toggle" IS '二次单击关闭。toggle: true|false 启用则单次激活功能并高亮图标，二次单击关闭功能并取消高亮';
COMMENT ON COLUMN "webgis_toolbar_button"."close_other_panel" IS '关闭其他面板。closePanel: true|false 打开时是否关闭其它打开的面板';
COMMENT ON COLUMN "webgis_toolbar_button"."more_properties" IS '更多属性。自定义属性，json';
COMMENT ON COLUMN "webgis_toolbar_button"."sort_sn" IS '排序序号。同工具条中的序号';
COMMENT ON COLUMN "webgis_toolbar_button"."description" IS '描述。';
COMMENT ON COLUMN "webgis_toolbar_button"."status" IS '状态。true:有效，false:停用';
COMMENT ON COLUMN "webgis_toolbar_button"."created_time" IS '创建时间。生成时间不可变更';
COMMENT ON COLUMN "webgis_toolbar_button"."created_by" IS '创建人。创建人user_id';
COMMENT ON COLUMN "webgis_toolbar_button"."updated_time" IS '修改时间。修改时更新';
COMMENT ON COLUMN "webgis_toolbar_button"."updated_by" IS '修改人。修改人user_id';
COMMENT ON COLUMN "webgis_toolbar_button"."sub_toolbar_id" IS '子工具条。关联webgis_toolbar的一个工具条id.不为空则为一个子工具条，不能是该按钮所属的工具条';
