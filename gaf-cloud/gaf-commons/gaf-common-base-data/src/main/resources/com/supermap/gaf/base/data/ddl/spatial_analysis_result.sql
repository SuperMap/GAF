-- liquibase formatted sql logicalFilePath:ddl/spatial_analysis_result

-- changeset SYS:20210406-0
CREATE TABLE "spatial_analysis_result" ("result_id" VARCHAR(36) NOT NULL, "result_conn_info" VARCHAR(255) NOT NULL, "result_data_set_name" VARCHAR(255), "created_time" TIMESTAMP WITHOUT TIME ZONE, "created_by" VARCHAR(255), "updated_time" TIMESTAMP WITHOUT TIME ZONE, "updated_by" VARCHAR(255), "analysis_name" VARCHAR(100) NOT NULL, "analysis_type" VARCHAR(255) NOT NULL);
COMMENT ON TABLE "spatial_analysis_result" IS '空间分析结果表';
COMMENT ON COLUMN "spatial_analysis_result"."result_id" IS '空间分析结果id。主键,uuid';
COMMENT ON COLUMN "spatial_analysis_result"."result_conn_info" IS '结果数据源连接信息。';
COMMENT ON COLUMN "spatial_analysis_result"."result_data_set_name" IS '结果数据集名称。';
COMMENT ON COLUMN "spatial_analysis_result"."created_time" IS '创建时间。';
COMMENT ON COLUMN "spatial_analysis_result"."created_by" IS '创建人。';
COMMENT ON COLUMN "spatial_analysis_result"."updated_time" IS '修改时间。';
COMMENT ON COLUMN "spatial_analysis_result"."updated_by" IS '修改人。';
COMMENT ON COLUMN "spatial_analysis_result"."analysis_name" IS '分析名称';
COMMENT ON COLUMN "spatial_analysis_result"."analysis_type" IS '分析类型（1：iobjects; 2：iserver; 3：bigdata）';