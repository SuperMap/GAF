-- liquibase formatted sql logicalFilePath:ddl/auth_resource_menu

-- changeset SYS:20210406-0
create table auth_resource_menu
(
    resource_menu_id varchar(36)  not null constraint auth_resource_menu_pkey primary key,
    parent_id        varchar(36),
    name             varchar(255) not null,
    target           varchar(3),
    path             varchar(255),
    url              varchar(500),
    icon             varchar(255),
    sort_sn          integer      not null,
    status           boolean,
    description      varchar(500),
    created_time     timestamp,
    created_by       varchar(255),
    updated_time     timestamp,
    updated_by       varchar(255)

);

comment on table auth_resource_menu is '菜单表';

comment on column auth_resource_menu.resource_menu_id is '菜单id。主键,uuid';

comment on column auth_resource_menu.parent_id is '父菜单id';

comment on column auth_resource_menu.name is '菜单名称';

comment on column auth_resource_menu.target is '打开方式。0:当前窗口(或当前窗口新开tab),1:新窗口打开';

comment on column auth_resource_menu.path is '前端路由路径。';

comment on column auth_resource_menu.url is '地址';

comment on column auth_resource_menu.icon is '图标。';

comment on column auth_resource_menu.sort_sn is '排序序号。同级中的序号';

comment on column auth_resource_menu.status is '状态。true:有效，false:无效';

comment on column auth_resource_menu.description is '描述。';

comment on column auth_resource_menu.created_time is '创建时间。生成时间不可变更';

comment on column auth_resource_menu.created_by is '创建人。创建人user_id';

comment on column auth_resource_menu.updated_time is '修改时间。修改时更新';

comment on column auth_resource_menu.updated_by is '修改人。修改人user_id';

