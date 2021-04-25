-- liquibase formatted sql logicalFilePath:ddl/data_workspace

-- changeset SYS:20210406-0
CREATE TABLE "data_workspace" ("workspace_id" VARCHAR(36) NOT NULL, "ws_name" VARCHAR(100) NOT NULL, "type_code" VARCHAR(50) NOT NULL, "server" VARCHAR(255) NOT NULL, "database" VARCHAR(50), "user_name" VARCHAR(100), "password" VARCHAR(100), "description" VARCHAR(500), "status" BOOLEAN, "tenant_id" VARCHAR(36), "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(50), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(50), "published" BOOLEAN DEFAULT FALSE, CONSTRAINT "data_workspace_pkey" PRIMARY KEY ("workspace_id"));
COMMENT ON TABLE "data_workspace" IS '工作空间表';
COMMENT ON COLUMN "data_workspace"."workspace_id" IS '工作空间id。主键,uuid';
COMMENT ON COLUMN "data_workspace"."ws_name" IS '名称。';
COMMENT ON COLUMN "data_workspace"."type_code" IS '类型。选自数据字典:工作空间类型，存各级code斜级分隔(level1code/level2code...)：
1.文件型-1:smwu,2:sxwx…
2.数据库型-1.PostgreSQL,2.MySQL,3.SQLPlus,4.MongoDB,5.PostGIS,6.OraclePlus
驱动信息(driver)存于字典的扩展字段';
COMMENT ON COLUMN "data_workspace"."server" IS '服务器名称。第1类文件路径+文件名，第2类服务器ip+端口或域名';
COMMENT ON COLUMN "data_workspace"."database" IS '数据库名称。';
COMMENT ON COLUMN "data_workspace"."user_name" IS '用户名。';
COMMENT ON COLUMN "data_workspace"."password" IS '密码。暂不保存（加密可解密）';
COMMENT ON COLUMN "data_workspace"."description" IS '描述。';
COMMENT ON COLUMN "data_workspace"."status" IS '状态。true:有效，false:无效';
COMMENT ON COLUMN "data_workspace"."tenant_id" IS '所属租户。所属租户id。空则是公共的';
COMMENT ON COLUMN "data_workspace"."created_time" IS '创建时间。生成时间不可变更';
COMMENT ON COLUMN "data_workspace"."created_by" IS '创建人。创建人user_id';
COMMENT ON COLUMN "data_workspace"."updated_time" IS '修改时间。修改时更新';
COMMENT ON COLUMN "data_workspace"."updated_by" IS '修改人。修改人user_id';
COMMENT ON COLUMN "data_workspace"."published" IS '是否已发布。true:已发布，false:未发布';