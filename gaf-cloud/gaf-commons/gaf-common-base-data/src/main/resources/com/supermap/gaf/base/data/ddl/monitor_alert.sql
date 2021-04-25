-- liquibase formatted sql logicalFilePath:ddl/monitor_alert

-- changeset SYS:20210406-0
CREATE TABLE "monitor_alert" ("grade" VARCHAR(255), "target" VARCHAR(255), "label" VARCHAR(255), "obj" VARCHAR(255), "value" VARCHAR(255), "unit" VARCHAR(255), "query" VARCHAR(1024));
COMMENT ON TABLE "monitor_alert" IS '监控警告记录表';