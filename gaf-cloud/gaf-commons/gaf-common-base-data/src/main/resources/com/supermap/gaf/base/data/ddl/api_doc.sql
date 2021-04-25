-- liquibase formatted sql logicalFilePath:ddl/api_doc

-- changeset SYS:20210406-0
CREATE TABLE "api_doc" ("id" VARCHAR(36) NOT NULL, "name" VARCHAR(255), "data" TEXT, "created_time" TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL, "updated_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_by" VARCHAR(255), CONSTRAINT "api_doc_pk" PRIMARY KEY ("id"));
COMMENT ON TABLE "api_doc" IS 'api doc文档';