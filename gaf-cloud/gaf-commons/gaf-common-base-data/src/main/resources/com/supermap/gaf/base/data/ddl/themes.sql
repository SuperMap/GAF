-- liquibase formatted sql logicalFilePath:ddl/themes

-- changeset SYS:20210406-0
CREATE TABLE "themes" ("id" VARCHAR(255) NOT NULL, "color" VARCHAR(255) DEFAULT 'red' NOT NULL, "multipleTabs" BOOLEAN, "user" VARCHAR(255) DEFAULT '' NOT NULL, CONSTRAINT "themes_pkey" PRIMARY KEY ("id"));
COMMENT ON TABLE "themes" IS '布局设置数据信息';