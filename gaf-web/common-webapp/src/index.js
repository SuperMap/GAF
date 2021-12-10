// @ts-nocheck
/**
 * Created by huanl on 2020/12/01
 */
import pkg from "../package";
//访问控制
import AuthRole from './authority/pages/AuthRole'
import AuthTenant from './authority/pages/AuthTenant'
import AuthUser from './authority/pages/AuthUser'
import AuthUserInfo from './authority/pages/AuthUserInfo'
import ComponentApi from './authority/pages/ComponentApi'
import DepartmentPostManage from './authority/pages/DepartmentPostManage'
import Menu from './authority/pages/Menu'
import RoleMenu from './authority/pages/RoleMenu'
import dics from './authority/pages/dics'
import RoleApi from './authority/pages/RoleApi'
//认证中心
import login from './authentication/pages/login'
//服务治理
import config from './governance/pages/config'
import logs from './governance/pages/logs'
import metrics from './governance/pages/metrics'
import routes from './governance/pages/routes'
import trace from './governance/pages/trace'
//地图资源
import WebgisButton from './map/pages/WebgisButton'
import WebgisCatalogLayer from './map/pages/WebgisCatalogLayer'
import WebgisResourceCatalog from './map/pages/WebgisResourceCatalog'
import WebgisService from './map/pages/WebgisService'
import WebgisToolbar from './map/pages/WebgisToolbar'
import WebgisToolbarButton from './map/pages/WebgisToolbarButton'
import WebgisOnlineMapping from './map/pages/index.vue'
//资源监控
import containers from './monitor/pages/containers'
import hosts from './monitor/pages/hosts'
//
import DataWorkspace from './dataMgt/pages/DataWorkspace'
import SpaceDatasource from './dataMgt/pages/SpaceDatasource'
import SpaceDatasourceTemplate from './dataMgt/pages/SpaceDatasourceTemplate'
import DataRights from './dataMgt/pages/DataRights'
import SysResourceDatasource from './dataMgt/pages/SysResourceDatasource'

import StorageManagement from './storage/pages/StorageManagement'

const components = [
    //访问控制
    AuthRole, AuthTenant,
    AuthUser, ComponentApi, DepartmentPostManage,
    Menu, RoleMenu, SysResourceDatasource,
    dics,RoleApi,
    //认证中心
    login,
    //服务治理
    config, logs, metrics, routes, trace,
    //地图资源
    WebgisButton, WebgisCatalogLayer,
    WebgisToolbar,  WebgisResourceCatalog, WebgisService, WebgisToolbarButton, WebgisOnlineMapping,
    //容器监控
    containers,hosts,
    //
    DataWorkspace,SpaceDatasource,SpaceDatasourceTemplate,DataRights,
    //
    StorageManagement
];

const install = function(Vue) {
    components.forEach((component) => {
        Vue.component(component.name, component);
    });
};

/* istanbul ignore if */
if (typeof window !== "undefined" && window.Vue) {
    install(window.Vue);
}

export {
    //访问控制
    AuthRole,
    AuthTenant,
    AuthUser,
    AuthUserInfo,
    ComponentApi,
    DepartmentPostManage,
    Menu,
    RoleMenu,
    SysResourceDatasource,
    dics,
    RoleApi,
    //认证中心
    login,
    //服务治理
    config,
    logs,
    metrics,
    routes,
    trace,
    //地图资源
    WebgisButton,
    WebgisCatalogLayer,
    WebgisResourceCatalog,
    WebgisService,
    WebgisToolbar,
    WebgisToolbarButton,
    WebgisOnlineMapping,
    //容器监控
    containers,
    hosts,
    //
    DataWorkspace,SpaceDatasource,SpaceDatasourceTemplate,DataRights,
    //
    StorageManagement
};

export default {
    version: pkg.version,
    install,
};
