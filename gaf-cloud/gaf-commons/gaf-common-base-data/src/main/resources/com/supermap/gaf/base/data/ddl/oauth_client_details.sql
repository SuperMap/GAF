-- liquibase formatted sql logicalFilePath:ddl/oauth_client_details

-- changeset SYS:20210406-0
CREATE TABLE "oauth_client_details" ("client_id" VARCHAR(256) NOT NULL, "resource_ids" VARCHAR(256), "client_secret" VARCHAR(256), "scope" VARCHAR(256), "authorized_grant_types" VARCHAR(256), "web_server_redirect_uri" VARCHAR(256), "authorities" VARCHAR(256), "access_token_validity" INTEGER, "refresh_token_validity" INTEGER, "additional_information" VARCHAR(4096), "autoapprove" VARCHAR(256), CONSTRAINT "oauth_client_details_pkey" PRIMARY KEY ("client_id"));
COMMENT ON TABLE "oauth_client_details" IS 'oauth2 client表';
