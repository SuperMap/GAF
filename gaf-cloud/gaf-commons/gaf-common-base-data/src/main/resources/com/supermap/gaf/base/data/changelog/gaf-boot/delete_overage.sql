-- liquibase formatted sql logicalFilePath:gaf-boot/delete_overage.sql

-- changeset SYS:20210425-0
DELETE FROM sys_component WHERE sys_component_id = '11009340-a17b-4e0e-97fd-cfeca718fa4f';
DELETE FROM sys_catalog WHERE sys_component_id = '11009340-a17b-4e0e-97fd-cfeca718fa4f' AND type = '2';
DELETE FROM sys_catalog WHERE sys_component_id = '11009340-a17b-4e0e-97fd-cfeca718fa4f' AND type = '1';
DELETE FROM auth_resource_module WHERE sys_component_id = '11009340-a17b-4e0e-97fd-cfeca718fa4f';
DELETE FROM sys_catalog WHERE catalog_id = 'm98c3751e-c893-11e9-8b31-223e840cdc8';
DELETE FROM auth_resource_menu WHERE menu_catalog_id = 'm98c3751e-c893-11e9-8b31-223e840cdc8';
DELETE FROM auth_role_menu WHERE resource_menu_id IN ('83ea5ee5-01d6-44f5-b8ba-1c5b6ec75660','853a5019-61b0-4665-9c0a-16dae8929005','3e4f18da-c6ba-4d4d-b996-722a9ad2589c','10f7e107-7d68-4327-b7be-d83a346f1f11','0d125d89-3c3d-4868-bbfd-2136a32ed86f');

DELETE FROM sys_component WHERE sys_component_id = '457db623-833c-4500-bf36-23e80300d86b';
DELETE FROM sys_catalog WHERE sys_component_id = '457db623-833c-4500-bf36-23e80300d86b' AND type = '2';
DELETE FROM sys_catalog WHERE sys_component_id = '457db623-833c-4500-bf36-23e80300d86b' AND type = '1';
DELETE FROM auth_resource_module WHERE sys_component_id = '457db623-833c-4500-bf36-23e80300d86b';
DELETE FROM sys_catalog WHERE catalog_id = 'd60fb7a3-d578-4ed9-aaee-56b721baa247';
DELETE FROM auth_resource_menu WHERE menu_catalog_id = 'd60fb7a3-d578-4ed9-aaee-56b721baa247';
DELETE FROM auth_role_menu WHERE resource_menu_id IN ('ef8d8f10-16e7-4cd5-b274-57d9397ef1e9','99d5fdf2-406a-49cf-b47c-b5c2eb978c74');




-- changeset SYS:20210712-0
DELETE FROM sys_catalog WHERE catalog_id = '2760fc79-838e-4463-bfef-e6b593de7113';
DELETE FROM sys_catalog WHERE catalog_id = '67aeecdb-91b3-4228-b0ec-6d2da717c840';
DELETE FROM sys_catalog WHERE catalog_id = 'f9772eb7-40fd-4373-b6f5-7d2ace6bf156';
DELETE FROM sys_catalog WHERE catalog_id = '2d224859-5aba-44c1-9ab9-740f850294e2';
DELETE FROM sys_catalog WHERE catalog_id = '83926137-1c8a-45eb-95f9-b1d020a138e4';
DELETE FROM sys_catalog WHERE catalog_id = '64646f21-1239-41dd-89c9-e5368265408c';
DELETE FROM sys_catalog WHERE catalog_id = 'c953872d-b4a7-4cbe-9943-7275a3bef81f';