CREATE TABLE "storage_permission" (
  "resource" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "scope" varchar COLLATE "pg_catalog"."default" NOT NULL,
  "ower" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "config_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL DEFAULT ''::character varying,
  "parent_resource" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL
)
;
COMMENT ON COLUMN "storage_permission"."resource" IS '资源。如xx/xx.jpg, xx/';

COMMENT ON COLUMN "storage_permission"."scope" IS '范围。download,upload,share,delete';

COMMENT ON COLUMN "storage_permission"."ower" IS '所有者';

COMMENT ON COLUMN "storage_permission"."config_name" IS '存储配置name';

COMMENT ON COLUMN "storage_permission"."parent_resource" IS '父级资源';

COMMENT ON COLUMN "storage_permission"."id" IS 'id';