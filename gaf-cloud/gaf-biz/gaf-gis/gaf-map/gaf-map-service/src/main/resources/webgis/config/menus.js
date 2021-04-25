/**
 * Created by huanl on 2019/8/24
 */
; (function (root) {
  root.GAF = root.GAF || {};
  var data ={user:{"realName":"系统管理员","tenants":[],"currentTenantId":"","roles":["ADMIN"],"groups":[],"userName":"admin","sysRoles":["ADMIN"],"token":""},config:{"color":"gray","configInfo":"[{\"id\":0.5667632072177766,\"icon\":\"table\",\"title\":\"栅格布局\",\"name\":\"grid\",\"columns\":[{\"title\":\"col1\",\"name\":\"栅格1\",\"span\":16,\"list\":[{\"id\":0.7146474097817785,\"icon\":\"radar-chart\",\"title\":\"平台概况\",\"name\":\"PlatResource\"}],\"id\":0.6008631089627439},{\"title\":\"col2\",\"name\":\"栅格2\",\"span\":8,\"list\":[{\"id\":0.0516170777617837,\"icon\":\"bars\",\"title\":\"通知公告\",\"name\":\"PlatList\",\"attr\":{\"mode\":\"define\",\"url\":\"\",\"data\":[\"12月31日，GAF平台β版本发布\",\"12月，国土空间基础信息平台立项\",\"5月18日，GAF1.0大数据开发帮助文档\",\"4月18日，GAF1.0开发规范文档发布\",\"3月18日，GAF1.0版本第一次交流会通知\"]}}],\"id\":0.8348182278931475}]},{\"id\":0.02516052020167825,\"icon\":\"table\",\"title\":\"栅格布局\",\"name\":\"grid\",\"columns\":[{\"title\":\"col1\",\"name\":\"栅格1\",\"span\":16,\"list\":[{\"id\":0.8526584733003963,\"icon\":\"ordered-list\",\"title\":\"GAF一站式企业协同研发云\",\"name\":\"PlatStep\",\"attr\":{\"direction\":\"horizontal\",\"mode\":\"define\",\"url\":\"\",\"nodes\":[{\"title\":\"需求\",\"desc\":\" \",\"icon\":\"question\"},{\"title\":\"编码\",\"desc\":\" \",\"icon\":\"apartment\"},{\"title\":\"测试\",\"desc\":\" \",\"icon\":\"safety-certificate\"},{\"title\":\"发布\",\"desc\":\" \",\"icon\":\"upload\"},{\"title\":\"反馈\",\"desc\":\" \",\"icon\":\"team\"}]}}],\"id\":0.28614559864034916},{\"title\":\"col2\",\"name\":\"栅格2\",\"span\":8,\"list\":[{\"id\":0.37871750700339635,\"icon\":\"ordered-list\",\"title\":\"产品日志\",\"name\":\"PlatStep\",\"attr\":{\"direction\":\"vertical\",\"mode\":\"define\",\"url\":\"\",\"nodes\":[{\"title\":\"2019年2月\",\"desc\":\"GAF 商业计划决策\",\"icon\":\"lock\"},{\"title\":\"2019年1月\",\"desc\":\"GAF 立项决策\",\"icon\":\"apartment\"}]}}],\"id\":0.13957135963375333}]},{\"id\":0.819758058528611,\"icon\":\"table\",\"title\":\"栅格布局\",\"name\":\"grid\",\"columns\":[{\"title\":\"col1\",\"name\":\"栅格1\",\"span\":6,\"list\":[{\"id\":0.04730618160569566,\"icon\":\"link\",\"title\":\"快速导航\",\"name\":\"PlatQuickLink\",\"attr\":{\"icon\":\"cloud-upload\",\"link\":\"http://gaf.supermap.pub/iportal\",\"embed\":false,\"size\":200,\"title\":\"GIS应用门户\"}}],\"id\":0.7451927839755528},{\"title\":\"col2\",\"name\":\"栅格2\",\"span\":6,\"list\":[{\"id\":0.5423831604146665,\"icon\":\"link\",\"title\":\"快速导航\",\"name\":\"PlatQuickLink\",\"attr\":{\"icon\":\"cloud-download\",\"link\":\"http://gaf.supermap.pub/iserver\",\"embed\":false,\"size\":200,\"title\":\"GIS应用服务器\"}}],\"id\":0.3808417387221108},{\"title\":\"col2\",\"name\":\"栅格2\",\"span\":6,\"list\":[{\"id\":0.5445573393617607,\"icon\":\"link\",\"title\":\"快速导航\",\"name\":\"PlatQuickLink\",\"attr\":{\"icon\":\"dot-chart\",\"link\":\"http://gaf.supermap.pub/report/DesignCenter\",\"embed\":false,\"size\":200,\"title\":\"报表设计中心\"}}],\"id\":0.2435911753595854},{\"title\":\"col2\",\"name\":\"栅格2\",\"span\":6,\"list\":[{\"id\":0.35609869407343253,\"icon\":\"link\",\"title\":\"快速导航\",\"name\":\"PlatQuickLink\",\"attr\":{\"icon\":\"radar-chart\",\"link\":\"http://igaf.supermap.pub/map/view/index.html#/\",\"embed\":false,\"size\":200,\"title\":\"地图应用\"}}],\"id\":0.1922175204674701}]}]","defaultConfigInfo":"[{\"id\":0.5667632072177766,\"icon\":\"table\",\"title\":\"栅格布局\",\"name\":\"grid\",\"columns\":[{\"title\":\"col1\",\"name\":\"栅格1\",\"span\":16,\"list\":[{\"id\":0.7146474097817785,\"icon\":\"radar-chart\",\"title\":\"平台概况\",\"name\":\"PlatResource\"}],\"id\":0.6008631089627439},{\"title\":\"col2\",\"name\":\"栅格2\",\"span\":8,\"list\":[{\"id\":0.0516170777617837,\"icon\":\"bars\",\"title\":\"通知公告\",\"name\":\"PlatList\",\"attr\":{\"mode\":\"define\",\"url\":\"\",\"data\":[\"12月31日，GAF平台β版本发布\",\"12月，国土空间基础信息平台立项\",\"5月18日，GAF1.0大数据开发帮助文档\",\"4月18日，GAF1.0开发规范文档发布\",\"3月18日，GAF1.0版本第一次交流会通知\"]}}],\"id\":0.8348182278931475}]},{\"id\":0.02516052020167825,\"icon\":\"table\",\"title\":\"栅格布局\",\"name\":\"grid\",\"columns\":[{\"title\":\"col1\",\"name\":\"栅格1\",\"span\":16,\"list\":[{\"id\":0.8526584733003963,\"icon\":\"ordered-list\",\"title\":\"GAF一站式企业协同研发云\",\"name\":\"PlatStep\",\"attr\":{\"direction\":\"horizontal\",\"mode\":\"define\",\"url\":\"\",\"nodes\":[{\"title\":\"需求\",\"desc\":\" \",\"icon\":\"question\"},{\"title\":\"编码\",\"desc\":\" \",\"icon\":\"apartment\"},{\"title\":\"测试\",\"desc\":\" \",\"icon\":\"safety-certificate\"},{\"title\":\"发布\",\"desc\":\" \",\"icon\":\"upload\"},{\"title\":\"反馈\",\"desc\":\" \",\"icon\":\"team\"}]}}],\"id\":0.28614559864034916},{\"title\":\"col2\",\"name\":\"栅格2\",\"span\":8,\"list\":[{\"id\":0.37871750700339635,\"icon\":\"ordered-list\",\"title\":\"进度控制\",\"name\":\"PlatStep\",\"attr\":{\"direction\":\"vertical\",\"mode\":\"define\",\"url\":\"\",\"nodes\":[{\"title\":\"2019年2月\",\"desc\":\"GAF 商业计划决策\",\"icon\":\"lock\"},{\"title\":\"2019年1月\",\"desc\":\"GAF 立项决策\",\"icon\":\"apartment\"}]}}],\"id\":0.13957135963375333}]}]","layoutType":"group","loginAdress":"/login","logo":"./img/logo.png","logoutAdress":"/logout","multipleTabs":true,"profileAdress":"/portal/user/profile","title":"GAF","user":"admin"},navs:[{"children":[],"embed":true,"embedUrl":"https://gitlab.supermap.pub/dashboard/projects","flag":"","icon":"icon-gateway","id":"04ec24b3-c899-11e9-8b31-223e840cdc84","name":"continuecompiledeploy","order":4,"path":"/ci","pid":"765c763e-c898-11e9-8b31-223e840cdc84","target":"1","visible":true},{"children":[],"embed":true,"embedUrl":"/management/view/index.html#/gateway","flag":"","icon":"icon-share-alt","id":"072a54f1-c896-11e9-8b31-223e840cdc84","name":"gateway","order":6,"path":"/gateway","pid":"98c3751e-c893-11e9-8b31-223e840cdc84","target":"0","visible":true},{"children":[],"embed":true,"embedUrl":"/log/view/index.html#/servicelog","flag":"","icon":"icon-eye","id":"09ac8e09-c894-11e9-8b31-223e840cdc84","name":"servicelog","order":5,"path":"/servicelog","pid":"98c3751e-c893-11e9-8b31-223e840cdc84","target":"0","visible":true},{"children":[],"embed":true,"embedUrl":"/datacenter/view/index.html#/dbmanager/services","flag":"","icon":"icon-edit","id":"0d3420f2-f07e-11e9-9957-26a09c2107cc","name":"服务信息管理","order":3,"path":"/dbmanager/services","pid":"2b06ee33-e0fe-11e9-9957-26a09c2107cc","target":"0","visible":true},{"children":[],"embed":true,"embedUrl":"/management/view/index.html#/resourcemonitor","flag":"","icon":"icon-cluster","id":"16c6ff89-c893-11e9-8b31-223e840cdc84","name":"resourcemonitor","order":1,"path":"/resourcemonitor","pid":"bc88ec4d-c892-11e9-8b31-223e840cdc84","target":"0","visible":true},{"children":[],"embed":false,"embedUrl":"","flag":"","icon":"icon-block","id":"2b06ee33-e0fe-11e9-9957-26a09c2107cc","name":"database","order":11,"path":"","pid":"","target":"","visible":true},{"children":[],"embed":true,"embedUrl":"/security/view/index.html#/authapps","flag":"","icon":"icon-copyright","id":"326c6a1c-c897-11e9-8b31-223e840cdc84","name":"authapps","order":7,"path":"/authapps","pid":"787555a1-c896-11e9-8b31-223e840cdc84","target":"0","visible":true},{"children":[],"embed":false,"embedUrl":"","flag":"","icon":"icon-home","id":"374f8923-cedb-11e9-8b31-223e840cdc84","name":"门户管理","order":8,"path":"","pid":"","target":"","visible":true},{"children":[],"embed":true,"embedUrl":"/map/view/index.html#/map3d?code=map3d","flag":"","icon":"icon-video-camera","id":"394367ca-cad3-11e9-8b31-223e840cdc84","name":"map3d","order":2,"path":"/map3d","pid":"c99ea89b-cad2-11e9-8b31-223e840cdc84","target":"0","visible":true},{"children":[],"embed":true,"embedUrl":"/management/view/index.html#/hostmonitor","flag":"","icon":"icon-eye","id":"39691265-c893-11e9-8b31-223e840cdc84","name":"hostmonitor","order":2,"path":"/hostmonitor","pid":"bc88ec4d-c892-11e9-8b31-223e840cdc84","target":"0","visible":true},{"children":[],"embed":true,"embedUrl":"http://harbor.supermap.pub","flag":"","icon":"icon-block","id":"3a232dd2-c899-11e9-8b31-223e840cdc84","name":"imagemanager","order":5,"path":"/imagemanager","pid":"765c763e-c898-11e9-8b31-223e840cdc84","target":"1","visible":true},{"children":[],"embed":true,"embedUrl":"/datacenter/view/index.html#/dbmanager","flag":"","icon":"icon-block","id":"47b2eb0c-e0fe-11e9-9957-26a09c2107cc","name":"dbmanager","order":1,"path":"/dbmanager","pid":"2b06ee33-e0fe-11e9-9957-26a09c2107cc","target":"0","visible":true},{"children":[],"embed":true,"embedUrl":"/map/view/index.html#/map23d?code=map3d","flag":"","icon":"icon-eye","id":"4b5c759e-cad3-11e9-8b31-223e840cdc84","name":"map23d","order":3,"path":"/map23d","pid":"c99ea89b-cad2-11e9-8b31-223e840cdc84","target":"0","visible":true},{"children":[],"embed":false,"embedUrl":"","flag":"","icon":"icon-setting","id":"4fc3e149-cedb-11e9-8b31-223e840cdc84","name":"customization","order":1,"path":"/customization","pid":"374f8923-cedb-11e9-8b31-223e840cdc84","target":"","visible":true},{"children":[],"embed":false,"embedUrl":"","flag":"501e9910-f60c-11e9-9722-52f9447fdfb8.","icon":"icon-read","id":"501e9910-f60c-11e9-9722-52f9447fdfb8","name":"培训演示","order":1,"path":"","pid":"","target":"","visible":true},{"children":[],"embed":true,"embedUrl":"/management/view/index.html#/storageinfo","flag":"","icon":"icon-book","id":"5bfd7c8c-c893-11e9-8b31-223e840cdc84","name":"storageinfo","order":3,"path":"/storageinfo","pid":"bc88ec4d-c892-11e9-8b31-223e840cdc84","target":"0","visible":true},{"children":[],"embed":false,"embedUrl":"","flag":"","icon":"icon-apartment","id":"5cbc4033-cedb-11e9-8b31-223e840cdc84","name":"menus","order":2,"path":"/menus","pid":"374f8923-cedb-11e9-8b31-223e840cdc84","target":"","visible":true},{"children":[],"embed":true,"embedUrl":"http://gaf.supermap.pub/docs/api/index.html?api=security","flag":"","icon":"icon-tags","id":"5cd6c73f-c89f-11e9-8b31-223e840cdc84","name":"productAPI","order":3,"path":"api","pid":"a0f63d1d-c89b-11e9-8b31-223e840cdc84","target":"1","visible":true},{"children":[],"embed":true,"embedUrl":"/management/view/index.html#/servicegraph","flag":"","icon":"icon-radar-chart","id":"5d8f7ed9-e042-11e9-9957-26a09c2107cc","name":"servicegraph","order":4,"path":"/servicegraph","pid":"98c3751e-c893-11e9-8b31-223e840cdc84","target":"0","visible":true},{"children":[],"embed":false,"embedUrl":"","flag":"","icon":"icon-setting","id":"635a09a3-c897-11e9-8b31-223e840cdc84","name":"personCenter","order":7,"path":"","pid":"","target":"0","visible":false},{"children":[],"embed":true,"embedUrl":"/security/view/index.html#/tenants","flag":"","icon":"icon-copyright","id":"6633dfe7-c896-11e9-8b31-223e840cdc84","name":"tenants","order":5,"path":"/tenants","pid":"787555a1-c896-11e9-8b31-223e840cdc84","target":"0","visible":true},{"children":[],"embed":true,"embedUrl":"/map/view/index.html#/manager/mapmanager","flag":"","icon":"icon-read","id":"6acc94c9-cad3-11e9-8b31-223e840cdc84","name":"manager_mapmanager","order":4,"path":"/manager/mapmanager","pid":"c99ea89b-cad2-11e9-8b31-223e840cdc84","target":"0","visible":true},{"children":[],"embed":true,"embedUrl":"/map/view/index.html#/map?code=map2d","flag":"6e70ef7b-f60c-11e9-9722-52f9447fdfb8.","icon":"icon-picture","id":"6e70ef7b-f60c-11e9-9722-52f9447fdfb8","name":"map","order":1,"path":"/map","pid":"501e9910-f60c-11e9-9722-52f9447fdfb8","target":"0","visible":true},{"children":[],"embed":false,"embedUrl":"","flag":"","icon":"icon-shop","id":"765c763e-c898-11e9-8b31-223e840cdc84","name":"continueIntegration","order":4,"path":"","pid":"","target":"0","visible":true},{"children":[],"embed":true,"embedUrl":"/map/view/index.html#/manager/mapserviceconfig","flag":"","icon":"icon-project","id":"77f1d024-cad3-11e9-8b31-223e840cdc84","name":"manager_mapserviceconfig","order":5,"path":"/manager/mapserviceconfig","pid":"c99ea89b-cad2-11e9-8b31-223e840cdc84","target":"0","visible":true},{"children":[],"embed":false,"embedUrl":"","flag":"","icon":"icon-cluster","id":"787555a1-c896-11e9-8b31-223e840cdc84","name":"安全中心","order":6,"path":"","pid":"","target":"0","visible":true},{"children":[],"embed":true,"embedUrl":"http://gaf.supermap.pub/iportal","flag":"","icon":"icon-vertical-right","id":"7d42bbce-c89d-11e9-8b31-223e840cdc84","name":"gisportal","order":1,"path":"","pid":"f66acb50-c89c-11e9-8b31-223e840cdc84","target":"1","visible":true},{"children":[],"embed":true,"embedUrl":"/management/view/index.html#/networkmonitor","flag":"","icon":"icon-gateway","id":"815ca499-c893-11e9-8b31-223e840cdc84","name":"networkmonitor","order":4,"path":"/networkmonitor","pid":"bc88ec4d-c892-11e9-8b31-223e840cdc84","target":"0","visible":true},{"children":[],"embed":true,"embedUrl":"/configcenter/view/index.html#/configserverinfolist","flag":"","icon":"icon-setting","id":"83a04146-d447-11e9-ba08-f6c8a93dd018","name":"configuration","order":7,"path":"/configmgt","pid":"98c3751e-c893-11e9-8b31-223e840cdc84","target":"0","visible":true},{"children":[],"embed":true,"embedUrl":"/security/view/index.html#/users","flag":"8fe3043d-c896-11e9-8b31-223e840cdc84.","icon":"icon-user","id":"8fe3043d-c896-11e9-8b31-223e840cdc84","name":"users","order":1,"path":"/users","pid":"787555a1-c896-11e9-8b31-223e840cdc84","target":"0","visible":true},{"children":[],"embed":true,"embedUrl":"/security/view/index.html#/users/password","flag":"917a5178-c897-11e9-8b31-223e840cdc84.","icon":"icon-man","id":"917a5178-c897-11e9-8b31-223e840cdc84","name":"users_password","order":1,"path":"/users/password","pid":"635a09a3-c897-11e9-8b31-223e840cdc84","target":"0","visible":true},{"children":[],"embed":false,"embedUrl":"","flag":"","icon":"icon-login","id":"9488a422-e1b0-11e9-9957-26a09c2107cc","name":"订单","order":12,"path":"","pid":"","target":"","visible":true},{"children":[],"embed":false,"embedUrl":"","flag":"","icon":"icon-cloud-server","id":"98c3751e-c893-11e9-8b31-223e840cdc84","name":"serviceGovernance","order":3,"path":"","pid":"","target":"0","visible":true},{"children":[],"embed":true,"embedUrl":"http://gaf.supermap.pub/iserver","flag":"99722fe0-c89d-11e9-8b31-223e840cdc84.","icon":"icon-notification","id":"99722fe0-c89d-11e9-8b31-223e840cdc84","name":"gisServer","order":2,"path":"","pid":"f66acb50-c89c-11e9-8b31-223e840cdc84","target":"1","visible":true},{"children":[],"embed":false,"embedUrl":"","flag":"","icon":"icon-question","id":"a0f63d1d-c89b-11e9-8b31-223e840cdc84","name":"productManual","order":9,"path":"","pid":"","target":"","visible":true},{"children":[],"embed":true,"embedUrl":"/security/view/index.html#/roles","flag":"","icon":"icon-team","id":"a627a2b4-c896-11e9-8b31-223e840cdc84","name":"roles","order":3,"path":"/roles","pid":"787555a1-c896-11e9-8b31-223e840cdc84","target":"0","visible":true},{"children":[],"embed":true,"embedUrl":"http://gaf.supermap.pub/iserver/","flag":"","icon":"icon-vertical-left","id":"b2c719c9-e1b2-11e9-9957-26a09c2107cc","name":"iserver","order":12,"path":"/iserver1","pid":"9488a422-e1b0-11e9-9957-26a09c2107cc","target":"0","visible":true},{"children":[],"embed":true,"embedUrl":"/management/view/index.html#/servicelist","flag":"","icon":"icon-woman","id":"b6e1e535-c893-11e9-8b31-223e840cdc84","name":"servicelist","order":3,"path":"/servicelist","pid":"98c3751e-c893-11e9-8b31-223e840cdc84","target":"0","visible":true},{"children":[],"embed":true,"embedUrl":"/management/view/index.html#/tplmanage","flag":"","icon":"icon-copy","id":"b7ed3374-c894-11e9-8b31-223e840cdc84","name":"tplmanage","order":1,"path":"/tplmanage","pid":"98c3751e-c893-11e9-8b31-223e840cdc84","target":"0","visible":true},{"children":[],"embed":true,"embedUrl":"/security/view/index.html#/departments","flag":"","icon":"icon-cluster","id":"bb4b8ec6-c896-11e9-8b31-223e840cdc84","name":"departments","order":2,"path":"/departments","pid":"787555a1-c896-11e9-8b31-223e840cdc84","target":"0","visible":true},{"children":[],"embed":false,"embedUrl":"","flag":"","icon":"icon-appstore","id":"bc88ec4d-c892-11e9-8b31-223e840cdc84","name":"resourceCenter","order":2,"path":"","pid":"","target":"0","visible":true},{"children":[],"embed":true,"embedUrl":"https://gitlab.supermap.pub/","flag":"","icon":"icon-copyright","id":"bd5391af-c898-11e9-8b31-223e840cdc84","name":"codemanager","order":2,"path":"/code","pid":"765c763e-c898-11e9-8b31-223e840cdc84","target":"1","visible":true},{"children":[],"embed":true,"embedUrl":"http://gaf.supermap.pub/report/DesignCenter","flag":"be6925e6-c89d-11e9-8b31-223e840cdc84.","icon":"icon-apartment","id":"be6925e6-c89d-11e9-8b31-223e840cdc84","name":"report_designcenter","order":3,"path":"/report","pid":"f66acb50-c89c-11e9-8b31-223e840cdc84","target":"1","visible":true},{"children":[],"embed":false,"embedUrl":"","flag":"","icon":"icon-book","id":"c99ea89b-cad2-11e9-8b31-223e840cdc84","name":"地图应用","order":10,"path":"","pid":"f66acb50-c89c-11e9-8b31-223e840cdc84","target":"","visible":true},{"children":[],"embed":true,"embedUrl":"/security/view/index.html#/users/profile","flag":"","icon":"icon-user","id":"cbc36e39-cbd0-11e9-8b31-223e840cdc84","name":"users_profile","order":1,"path":"/users/profile","pid":"635a09a3-c897-11e9-8b31-223e840cdc84","target":"0","visible":true},{"children":[],"embed":true,"embedUrl":"https://doc.supermap.pub","flag":"","icon":"icon-check","id":"cfa3cf53-c89b-11e9-8b31-223e840cdc84","name":"developermanual","order":1,"path":"/devops","pid":"a0f63d1d-c89b-11e9-8b31-223e840cdc84","target":"1","visible":false},{"children":[],"embed":true,"embedUrl":"/management/view/index.html#/appmanage","flag":"","icon":"icon-picture","id":"d61df27f-c894-11e9-8b31-223e840cdc84","name":"appmanage","order":2,"path":"/appmanage","pid":"98c3751e-c893-11e9-8b31-223e840cdc84","target":"0","visible":true},{"children":[],"embed":true,"embedUrl":"/management/view/index.html#/servicemonitor","flag":"","icon":"icon-cloud-download","id":"d77c4b8a-c893-11e9-8b31-223e840cdc84","name":"servicemonitor","order":4,"path":"/servicemonitor","pid":"98c3751e-c893-11e9-8b31-223e840cdc84","target":"0","visible":true},{"children":[],"embed":true,"embedUrl":"/security/view/index.html#/tenantusers","flag":"","icon":"icon-team","id":"e1fe24fd-dff8-11e9-9957-26a09c2107cc","name":"tenantusers","order":6,"path":"/tenantusers","pid":"787555a1-c896-11e9-8b31-223e840cdc84","target":"0","visible":true},{"children":[],"embed":true,"embedUrl":"http://sonar.supermap.pub","flag":"","icon":"icon-crown","id":"e34b8491-c898-11e9-8b31-223e840cdc84","name":"codereview","order":3,"path":"/sonar","pid":"765c763e-c898-11e9-8b31-223e840cdc84","target":"1","visible":true},{"children":[],"embed":true,"embedUrl":"/management/view/index.html#/projects","flag":"","icon":"icon-apartment","id":"ecf13111-c896-11e9-8b31-223e840cdc84","name":"projects","order":1,"path":"/projects","pid":"765c763e-c898-11e9-8b31-223e840cdc84","target":"0","visible":true},{"children":[],"embed":true,"embedUrl":"/security/view/index.html#/resources","flag":"","icon":"icon-user","id":"effa3055-d07b-11e9-8c88-223e840cdc84","name":"resources","order":4,"path":"/resources","pid":"787555a1-c896-11e9-8b31-223e840cdc84","target":"0","visible":true},{"children":[],"embed":false,"embedUrl":"","flag":"","icon":"icon-login","id":"f66acb50-c89c-11e9-8b31-223e840cdc84","name":"portalApp","order":1,"path":"","pid":"","target":"0","visible":true},{"children":[],"embed":true,"embedUrl":"/datacenter/view/index.html#/dbmanager/serviceinstances","flag":"","icon":"icon-ordered-list","id":"fdc21dc5-e18a-11e9-9957-26a09c2107cc","name":"dbmanager_serviceinstances","order":2,"path":"/dbmanager/serviceinstances","pid":"2b06ee33-e0fe-11e9-9957-26a09c2107cc","target":"0","visible":true},{"children":[],"embed":true,"embedUrl":"/map/view/index.html#/map?code=map2d","flag":"","icon":"icon-picture","id":"feb955be-cad2-11e9-8b31-223e840cdc84","name":"map","order":1,"path":"/map","pid":"c99ea89b-cad2-11e9-8b31-223e840cdc84","target":"0","visible":true},{"children":[],"embed":true,"embedUrl":"http://doc.supermap.pub/content/GAF平台/快速入门/","flag":"","icon":"icon-tags","id":"ff559766-c89b-11e9-8b31-223e840cdc84","name":"quickstart","order":2,"path":"/quickstart","pid":"a0f63d1d-c89b-11e9-8b31-223e840cdc84","target":"1","visible":true}]}
  data.navs = [
    {
      "embed": false,
      "embedUrl": "/authority/view/index.html#/RoleMenu",
      "icon": "",
      "id": "11",
      "name": "门户系统",
      "order": 5,
      "path": "/authority/RoleMenu",
      "pid": "",
      "target": false,
      "visible": true,
      "title": "门户系统"
    },
    {
      "embed": true,
      "embedUrl": "http://www.baidu.com",
      "icon": "",
      "id": "112",
      "name": "门户管理",
      "order": 5,
      "path": "/customzation",
      "pid": "11",
      "target": true,
      "visible": true,
      "title": "门户管理"
    },
    {
      "embed": false,
      "embedUrl": "http://www.baidu.com",
      "icon": "",
      "id": "c1a008f3-3a06-4b9f-8a0f-fd607960a15a112",
      "name": "菜单管理",
      "order": 5,
      "path": "/menus",
      "pid": "112",
      "target": false,
      "visible": true,
      "title": "菜单管理"
    },
    {
      "embed": false,
      "embedUrl": "",
      "icon": "",
      "id": "c1a008f3-3a06-4b9f-8a0f-fd607960a1512a112",
      "name": "门户管理",
      "order": 5,
      "path": "/customization",
      "pid": "112",
      "target": false,
      "visible": true,
      "title": "门户管理"
    },
    {
      "embed": true,
      "embedUrl": "/authority/view/index.html#/RoleMenu",
      "icon": "",
      "id": "c1a008f3-3a06-4b9f-8a0f-fd607960a15a1",
      "name": "访问控制系统",
      "order": 5,
      "path": "/authority/RoleMenu",
      "pid": "",
      "target": false,
      "visible": true,
      "title": "访问控制系统"
    },
    {
      "embed": true,
      "embedUrl": "/authority/view/index.html#/RoleMenu",
      "icon": "",
      "id": "c1a008f3-3a06-4b9f-8a0f-fd607960a15a",
      "name": "角色分配菜单",
      "order": 5,
      "path": "/authority/RoleMenu",
      "pid": "ee16de93-f3a6-4248-9743-888b647e0e43",
      "target": true,
      "visible": true,
      "title": "角色分配菜单"
    },
    {
      "embed": true,
      "embedUrl": "/authority/view/index.html#/Menu",
      "icon": "",
      "id": "c53fd5ff-856f-4a73-a213-f8df90dc34d0",
      "name": "菜单管理",
      "order": 5,
      "path": "/authority/Menu",
      "pid": "251c9892-cf95-45ed-8f0a-c3ba9d6880eb",
      "target": false,
      "visible": true,
      "title": "菜单管理"
    },
    {
      "embed": true,
      "embedUrl": "/authority/view/index.html#/DepartmentPostManage",
      "icon": "",
      "id": "7490fc37-7b50-4fb4-aeb5-3a7c9afbafe7",
      "name": "部门岗位管理",
      "order": 2,
      "path": "/authority/DepartmentPostManage",
      "pid": "ee16de93-f3a6-4248-9743-888b647e0e43",
      "target": false,
      "visible": true,
      "title": "部门岗位管理"
    },
    {
      "embed": true,
      "embedUrl": "/authority/view/index.html#/AuthUser",
      "icon": "",
      "id": "9b9568d4-6313-4279-9a6d-34bd445ba220",
      "name": "用户管理",
      "order": 3,
      "path": "/authority/AuthUser",
      "pid": "ee16de93-f3a6-4248-9743-888b647e0e43",
      "target": false,
      "visible": true,
      "title": "用户管理"
    },
    {
      "embed": true,
      "embedUrl": "/authority/view/index.html#/AuthRole",
      "icon": "",
      "id": "e014fb60-2f09-4d8a-8ec5-c1501b71f8f3",
      "name": "角色管理",
      "order": 4,
      "path": "/authority/AuthRole",
      "pid": "ee16de93-f3a6-4248-9743-888b647e0e43",
      "target": false,
      "visible": true,
      "title": "角色管理"
    },
    {
      "embed": true,
      "embedUrl": "/authority/view/index.html#/RoleApi",
      "icon": "",
      "id": "0e4dbd4b-00ad-4b53-8c6c-7324f4fab8e3",
      "name": "角色分配API",
      "order": 6,
      "path": "/authority/RoleApi",
      "pid": "ee16de93-f3a6-4248-9743-888b647e0e43",
      "target": false,
      "visible": true,
      "title": "角色分配API"
    },
    {
      "embed": true,
      "embedUrl": "/authority/view/index.html#/AuthTenant",
      "icon": "",
      "id": "88ec31de-7acc-44bc-9faa-4dc1aaf7b90f",
      "name": "租户管理",
      "order": 1,
      "path": "/authority/AuthTenant",
      "pid": "ee16de93-f3a6-4248-9743-888b647e0e43",
      "target": false,
      "visible": true,
      "title": "租户管理"
    },
    {
      "embed": true,
      "embedUrl": "/authority/view/index.html#/SysComponent",
      "icon": "",
      "id": "04f31151-beb2-4f0b-bdc6-b0a9c4f52cba",
      "name": "组件管理",
      "order": 1,
      "path": "/authority/SysComponent",
      "pid": "251c9892-cf95-45ed-8f0a-c3ba9d6880eb",
      "target": false,
      "visible": true,
      "title": "组件管理"
    },
    {
      "embed": true,
      "embedUrl": "/authority/view/index.html#/ComponentModule",
      "icon": "",
      "id": "c1abb331-29f2-412e-9ac4-a8d1a6cc29ea",
      "name": "模块管理",
      "order": 2,
      "path": "/authority/ComponentModule",
      "pid": "251c9892-cf95-45ed-8f0a-c3ba9d6880eb",
      "target": false,
      "visible": true,
      "title": "模块管理"
    },
    {
      "embed": true,
      "embedUrl": "/authority/view/index.html#/ComponentApi",
      "icon": "",
      "id": "2c1ed7ce-7cb2-4da6-b986-9239802f3e52",
      "name": "API管理",
      "order": 3,
      "path": "/authority/ComponentApi",
      "pid": "251c9892-cf95-45ed-8f0a-c3ba9d6880eb",
      "target": false,
      "visible": true,
      "title": "API管理"
    },
    {
      "embed": true,
      "embedUrl": "/authority/view/index.html#/ModuleApi",
      "icon": "",
      "id": "9c1f4b41-0d6a-4e3d-b04a-b48d638049d8",
      "name": "模块关联API",
      "order": 4,
      "path": "/authority/ModuleApi",
      "pid": "251c9892-cf95-45ed-8f0a-c3ba9d6880eb",
      "target": false,
      "visible": true,
      "title": "模块关联API"
    },
    {
      "embed": false,
      "embedUrl": "",
      "icon": "",
      "id": "ee16de93-f3a6-4248-9743-888b647e0e43",
      "name": "权限控制",
      "order": 1,
      "path": "",
      "pid": "c1a008f3-3a06-4b9f-8a0f-fd607960a15a1",
      // "pid": "",
      "target": false,
      "visible": true,
      "title": "权限控制"
    },
    {
      "embed": false,
      "embedUrl": "",
      "icon": "",
      "id": "251c9892-cf95-45ed-8f0a-c3ba9d6880eb",
      "name": "系统管理",
      "order": 2,
      "path": "",
      "pid": "c1a008f3-3a06-4b9f-8a0f-fd607960a15a1",
      // "pid": "",
      "target": false,
      "visible": true,
      "title": "系统管理"
    }
  ]
  root.GAF.user = data.user;
  root.GAF.config = data.config;
  root.GAF.navs = data.navs
}) (this);
