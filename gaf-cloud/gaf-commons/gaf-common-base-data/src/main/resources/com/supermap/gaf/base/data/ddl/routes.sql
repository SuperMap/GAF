-- liquibase formatted sql logicalFilePath:ddl/routes

-- changeset SYS:20210406-0
CREATE TABLE "routes" ("id" VARCHAR(50) NOT NULL, "route_id" VARCHAR(64), "route_uri" VARCHAR(128), "route_order" INTEGER, "predicates" TEXT, "filters" TEXT, "enable" BOOLEAN, "create_time" TIMESTAMP WITHOUT TIME ZONE, "update_time" TIMESTAMP WITHOUT TIME ZONE, "type" VARCHAR(10), "tenant_id" VARCHAR(50), CONSTRAINT "routes_pkey" PRIMARY KEY ("id"));
COMMENT ON TABLE "routes" IS '路由表';
COMMENT ON COLUMN "routes"."route_id" IS '路由id';
COMMENT ON COLUMN "routes"."route_uri" IS '转发目标uri';
COMMENT ON COLUMN "routes"."route_order" IS '路由执行顺序';
COMMENT ON COLUMN "routes"."predicates" IS '断言字符串集合，字符串结构：jsonArray.toString';
COMMENT ON COLUMN "routes"."filters" IS '过滤器字符串集合，字符串结构：jsonArray.toString';
COMMENT ON COLUMN "routes"."enable" IS '是否启用';
COMMENT ON COLUMN "routes"."create_time" IS '创建时间';
COMMENT ON COLUMN "routes"."update_time" IS '修改时间';
COMMENT ON COLUMN "routes"."type" IS '类型';
COMMENT ON COLUMN "routes"."tenant_id" IS '租户id';
