-- liquibase formatted sql logicalFilePath:ddl/storage_s3_server

-- changeset SYS:20210728-0
CREATE TABLE "storage_s3_server" (
  "access_key" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "secret_key" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "service_endpoint" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL::character varying,
  CONSTRAINT "s3_server_pkey" PRIMARY KEY ("id")
)
;
