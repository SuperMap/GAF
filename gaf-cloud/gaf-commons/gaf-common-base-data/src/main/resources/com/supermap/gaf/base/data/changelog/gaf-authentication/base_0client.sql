-- liquibase formatted sql logicalFilePath:gaf-authentication/client

-- changeset SYS:20210406-0
INSERT INTO "oauth_client_details"("client_id", "resource_ids", "client_secret", "scope", "authorized_grant_types", "web_server_redirect_uri", "authorities", "access_token_validity", "refresh_token_validity", "additional_information", "autoapprove") VALUES ('custom_client', 'custom_client_rid', '$2a$10$cfHuOZPGdPU1LfPOsltQ9.FOfA6iD9R6FuQR.5rjd5AEd7qOl4eCq', 'all', 'password,authorization_code,refresh_token', 'http://localhost:10000', NULL, 7200, 72000, NULL, 'true');
