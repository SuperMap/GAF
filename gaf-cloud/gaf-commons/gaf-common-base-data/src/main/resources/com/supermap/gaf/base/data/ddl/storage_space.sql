-- liquibase formatted sql logicalFilePath:ddl/storage_space

-- changeset SYS:20210728-0
CREATE TABLE "storage_space" (
  "target_type" char(1) COLLATE "pg_catalog"."default" NOT NULL,
  "target" varchar COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "storage_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL::character varying,
  "parent_space_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  "created_type" char(1) COLLATE "pg_catalog"."default" NOT NULL,
  "description" varchar(255) COLLATE "pg_catalog"."default",
  "total_size" int8 NOT NULL DEFAULT '-1'::integer,
  "created_time" timestamp(6) NOT NULL DEFAULT now(),
  "updated_time" timestamp(6) NOT NULL DEFAULT now(),
  "name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
  CONSTRAINT "server_config_pkey" PRIMARY KEY ("id")
);
CREATE UNIQUE INDEX "storage_space_target_created_type_name_idx" ON "storage_space" USING btree (
  "target" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST,
  "created_type" COLLATE "pg_catalog"."default" "pg_catalog"."bpchar_ops" ASC NULLS LAST,
  "name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

COMMENT ON COLUMN "storage_space"."target_type" IS '目标对象类型。P:平台, T:租户';

COMMENT ON COLUMN "storage_space"."target" IS '目标对象。平台或者应用或者用户';

COMMENT ON COLUMN "storage_space"."storage_name" IS '物理名称（目录或者bucketName)';

COMMENT ON COLUMN "storage_space"."id" IS '逻辑空间id';

COMMENT ON COLUMN "storage_space"."parent_space_id" IS '父空间。s3server id或者space id';

COMMENT ON COLUMN "storage_space"."created_type" IS '创建类型。A：分配的，C:自己创建的';

COMMENT ON COLUMN "storage_space"."description" IS '描述';

COMMENT ON COLUMN "storage_space"."total_size" IS '空间大小';

COMMENT ON COLUMN "storage_space"."created_time" IS '创建时间。生成时间不可变更';

COMMENT ON COLUMN "storage_space"."updated_time" IS '修改时间。修改时更新';

COMMENT ON COLUMN "storage_space"."name" IS '名称。target,space_type下不重复';