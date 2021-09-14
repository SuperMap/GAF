-- liquibase formatted sql logicalFilePath:ddl/service_source

-- changeset SYS:20210914-0
CREATE TABLE "service_source" (
   "service_source_id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL,
   "source_id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL,
   "service_id" varchar(36) COLLATE "pg_catalog"."default" NOT NULL,
   "source_type" int2 NOT NULL,
   CONSTRAINT "rel_tile_service_copy1_pkey" PRIMARY KEY ("service_source_id"),
   CONSTRAINT "rel_tile_service_copy1_tile_id_service_id_key" UNIQUE ("source_id", "service_id")
)
;

COMMENT ON COLUMN "service_source"."service_source_id" IS '服务来源关联记录id';

COMMENT ON COLUMN "service_source"."source_id" IS '来源id';

COMMENT ON COLUMN "service_source"."service_id" IS '服务id';

COMMENT ON COLUMN "service_source"."source_type" IS '来源类型。1:工作空间；2:瓦片';

COMMENT ON TABLE "service_source" IS '服务来源关联表';;