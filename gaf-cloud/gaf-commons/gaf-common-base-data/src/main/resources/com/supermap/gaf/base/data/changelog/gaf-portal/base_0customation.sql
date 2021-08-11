-- liquibase formatted sql logicalFilePath:gaf-portal/customation
-- changeset SYS:20210406-0
INSERT INTO "public"."customation"("loginAdress", "logoutAdress", "profileAdress", "logo", "title", "color", "multipleTabs", "user", "layoutType", "configInfo", "defaultConfigInfo", "id", "tenant_id", "created_time", "created_by", "updated_time", "updated_by") VALUES ('/login', '/logout', '/viewer/#/authority/authuserinfo', './img/logo.png', 'GAF', '#545c64', 't', 'sys_admin', 'top', '[{"id":0.5667632072177766,"icon":"table","title":"栅格布局","name":"grid","columns":[{"title":"col1","name":"栅格1","span":16,"list":[{"id":0.7146474097817785,"icon":"radar-chart","title":"平台概况","name":"PlatResource"}],"id":0.6008631089627439},{"title":"col2","name":"栅格2","span":8,"list":[{"id":0.0516170777617837,"icon":"bars","title":"通知公告","name":"PlatList","attr":{"mode":"define","url":"","data":["2020年12月31日，GAF2.0发布","2020年7月31日，GAF1.0发布","12月31日，GAF平台β版本发布","5月18日，GAF1.0大数据开发帮助文档","4月18日，GAF1.0开发规范文档发布","2019年3月18日，GAF1.0版本第一次交流会通知"]}}],"id":0.8348182278931475}]},{"id":0.02516052020167825,"icon":"table","title":"栅格布局","name":"grid","columns":[{"title":"col1","name":"栅格1","span":16,"list":[{"id":0.8526584733003963,"icon":"ordered-list","title":"GAF一站式企业协同研发云","name":"PlatStep","attr":{"direction":"horizontal","mode":"define","url":"","nodes":[{"title":"需求","desc":" ","icon":"question"},{"title":"编码","desc":" ","icon":"apartment"},{"title":"测试","desc":" ","icon":"safety-certificate"},{"title":"发布","desc":" ","icon":"upload"},{"title":"反馈","desc":" ","icon":"team"}]}}],"id":0.28614559864034916},{"title":"col2","name":"栅格2","span":8,"list":[{"id":0.37871750700339635,"icon":"ordered-list","title":"产品日志","name":"PlatStep","attr":{"direction":"vertical","mode":"define","url":"","nodes":[{"title":"2019年2月","desc":"GAF 商业计划决策","icon":"lock"},{"title":"2019年1月","desc":"GAF 立项决策","icon":"apartment"}]}}],"id":0.13957135963375333}]},{"id":0.819758058528611,"icon":"table","title":"栅格布局","name":"grid","columns":[{"title":"col1","name":"栅格1","span":6,"list":[{"id":0.04730618160569566,"icon":"link","title":"快速导航","name":"PlatQuickLink","attr":{"icon":"cloud-upload","link":"http://gaf.supermap.pub/iportal","embed":false,"size":200,"title":"GIS应用门户"}}],"id":0.7451927839755528},{"title":"col2","name":"栅格2","span":6,"list":[{"id":0.5423831604146665,"icon":"link","title":"快速导航","name":"PlatQuickLink","attr":{"icon":"cloud-download","link":"http://gaf.supermap.pub/iserver","embed":false,"size":200,"title":"GIS应用服务器"}}],"id":0.3808417387221108},{"title":"col2","name":"栅格2","span":6,"list":[{"id":0.5445573393617607,"icon":"link","title":"快速导航","name":"PlatQuickLink","attr":{"icon":"dot-chart","link":"http://gaf.supermap.pub/report/DesignCenter","embed":false,"size":200,"title":"报表设计中心"}}],"id":0.2435911753595854},{"title":"col2","name":"栅格2","span":6,"list":[{"id":0.35609869407343253,"icon":"link","title":"快速导航","name":"PlatQuickLink","attr":{"icon":"radar-chart","link":"http://igaf.supermap.pub/map/view/index.html#/","embed":false,"size":200,"title":"地图应用"}}],"id":0.1922175204674701}]}]', NULL, '7c94f0c0-d27f-4402-b01b-83d74e2a48f2', 'tenant_000000', '2020-12-17 11:17:03.618182', 'SYS', '2021-03-31 01:43:38.224164', 'SYS');

-- changeset SYS:20210718-0
update customation set "layoutType" = 'topLeft' WHERE "id" = '7c94f0c0-d27f-4402-b01b-83d74e2a48f2';

-- changeset SYS:20210809-4
update customation set "color" = '#323944' WHERE "id" = '7c94f0c0-d27f-4402-b01b-83d74e2a48f2';