-- liquibase formatted sql logicalFilePath:ddl/auth_user

-- changeset SYS:20210406-0
create table auth_user
(
    user_id         varchar(36)          not null constraint auth_user_pkey primary key,
    tenant_id       varchar(36)          not null,
    department_id   varchar(36)          not null,
    sort_sn         integer,
    name            varchar(50)          not null,
    password        varchar(100),
    real_name       varchar(50)          not null,
    id_number       varchar(30),
    mobile_number   varchar(30),
    email           varchar(100),
    address         varchar(200),
    post_id         varchar(36),
    expiration_time timestamp,
    is_third_party  boolean,
    state           boolean default true not null,
    status          boolean default true not null,
    description     varchar(500),
    last_login_time timestamp,
    created_time    timestamp,
    created_by      varchar(255),
    updated_time    timestamp,
    updated_by      varchar(255)

);

comment on table auth_user is '用户表';

comment on column auth_user.user_id is '用户id。主键,uuid';

comment on column auth_user.tenant_id is '租户id。';

comment on column auth_user.department_id is '部门id。';

comment on column auth_user.sort_sn is '排序序号。同级中的序号';

comment on column auth_user.name is '登录用户名。登录名，可含字母数字下划线';

comment on column auth_user.password is '密码。存到认证中心，db可不存';

comment on column auth_user.real_name is '真实姓名。中文名称';

comment on column auth_user.id_number is '身份证号。';

comment on column auth_user.mobile_number is '手机号。';

comment on column auth_user.email is '邮箱。';

comment on column auth_user.address is '地址。';

comment on column auth_user.post_id is '岗位id。';

comment on column auth_user.expiration_time is '授权截止时间。为空则永不过期';

comment on column auth_user.is_third_party is '是否第三方。从所属部门继承该属性';

comment on column auth_user.state is '状态。true: 启用 false 禁用状态';

comment on column auth_user.status is '逻辑删除字段。true:有效，false:过期/无效';

comment on column auth_user.description is '描述。';

comment on column auth_user.last_login_time is '上次登录时间。';

comment on column auth_user.created_time is '创建时间。生成时间不可变更';

comment on column auth_user.created_by is '创建人。创建人user_id';

comment on column auth_user.updated_time is '修改时间。修改时更新';

comment on column auth_user.updated_by is '修改人。修改人user_id';

