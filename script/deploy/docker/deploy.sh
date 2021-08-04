#!/usr/bin/env bash


#使用说明
usage()
{
    cat << USAGE >&2
Usage:
	GAF应用部署脚本 deploy [COMMANDS]

	COMMANDS:

	base          一键部署基础GAF应用
	monitor       一键部署GAF的监控相关应用
	delete-all    一键删除所有GAF应用以及挂载卷
	boot          一键部署基础GAF-BOOT应用
USAGE
    exit 1
}

workspace() {
    #设置工作目录
    export Root_Current_Dir=$(cd `dirname $0`;pwd)
    cd $Root_Current_Dir
    #命令文件权限
    find $Root_Current_Dir -name "*.sh"|xargs chmod +x
    #加载GAF函数
    source $Root_Current_Dir/bin/gaf-func.sh
    #加载环境变量
    source $Root_Current_Dir/.env
    export GAF_BASE_DATA_PATH=`readlink -f $GAF_BASE_DATA_PATH`
}


base() {
    #设置工作目录
    workspace
    #检查命令
    check_commands docker docker-compose
    #开启防火墙规则
    port_gaf
    #创建docker网络
    create_docker_network gaf-net
    #创建挂载卷
    mkdir -p ${GAF_VOL_DIR}
    #拷贝挂载数据
    cp -rf $Root_Current_Dir/conf/GAF_ENV_CONFIG.env ${GAF_VOL_DIR}
    #替换GAF_ENV_CONFIG.env里的变量
    sed_config_env
    #启动GAF基础环境数据存储应用
    LOAD_SERVICE="gaf-postgres gaf-redis gaf-minio gaf-s3fs-mount"
    docker-compose up -d $LOAD_SERVICE
    #向postgres数据库导入基础数据
    wait_gaf_db
    source $Root_Current_Dir/conf/GAF_ENV_CONFIG.env
    docker run --rm --net=gaf-net -v $GAF_BASE_DATA_PATH:/opt/liquibase-data registry.cn-hangzhou.aliyuncs.com/supermap-gaf/build-tools:v1.0 \
        liquibase \
          --driver=$GAF_ENV_DATASOURCE_DRIVER \
          --classpath=/usr/local/liquibase/liquibase-classpath/postgresql-42.2.23.jar \
          --url=$GAF_ENV_DATASOURCE_URL \
          --username=$GAF_ENV_DATASOURCE_USERNAME \
          --password=$GAF_ENV_DATASOURCE_PASSWORD \
          --changeLogFile=liquibase-data/entry/gaf-cloud-base.xml \
          update
    #启动GAF优先启动应用
    LOAD_SERVICE="gaf-microservice-rigister gaf-microservice-conf"
    docker-compose up -d gaf-microservice-rigister gaf-microservice-conf
    #等待优先启动应用启动完成
    wait_container_health $LOAD_SERVICE
    #启动其他GAF基础应用
    LOAD_SERVICE="gaf-microservice-api gaf-microservice-gateway gaf-sys-mgt gaf-authentication gaf-authority gaf-microservice-governance gaf-portal gaf-map gaf-data-mgt gaf-storage gaf-analysis gaf-webapp gaf-mapapp"
    docker-compose up -d $LOAD_SERVICE
    #等待启动完成
    LOAD_SERVICE="gaf-microservice-api gaf-microservice-gateway gaf-sys-mgt gaf-authentication gaf-authority gaf-portal gaf-map gaf-data-mgt gaf-storage gaf-analysis"
    wait_container_health $LOAD_SERVICE

    #提示
    echo "启动GAF成功！！！GAF地址：http://${HOSTIP}:30777"
}

monitor() {
    #设置工作目录
    workspace
    #检查命令
    check_commands docker docker-compose
    #拷贝挂载数据
    cp -rf $Root_Current_Dir/data/vol/vol_fluentd-es ${GAF_VOL_DIR}
    cp -rf $Root_Current_Dir/data/vol/vol_grafana ${GAF_VOL_DIR}
    cp -rf $Root_Current_Dir/data/vol/vol_prometheus ${GAF_VOL_DIR}
    #向postgres数据库导入基础数据
    source $Root_Current_Dir/conf/GAF_ENV_CONFIG.env
    docker run --rm --net=gaf-net -v $GAF_BASE_DATA_PATH:/opt/liquibase-data registry.cn-hangzhou.aliyuncs.com/supermap-gaf/build-tools:v1.0 \
        liquibase \
          --driver=$GAF_ENV_DATASOURCE_DRIVER \
          --classpath=/usr/local/liquibase/liquibase-classpath/postgresql-42.2.23.jar \
          --url=$GAF_ENV_DATASOURCE_URL \
          --username=$GAF_ENV_DATASOURCE_USERNAME \
          --password=$GAF_ENV_DATASOURCE_PASSWORD \
          --changeLogFile=liquibase-data/entry/gaf-monitor.xml \
          update
    docker run --rm --net=gaf-net -e PGPASSWORD=$GAF_ENV_DATASOURCE_PASSWORD registry.cn-hangzhou.aliyuncs.com/supermap-gaf/build-tools:v1.0 createdb -h gaf-postgres -p 5432 -U $GAF_ENV_DATASOURCE_USERNAME grafana
    #TODO 使用api异步导入grafana的基础数据
    #启动GAF基础环境监控应用
    LOAD_SERVICE="gaf-elasticsearch gaf-fluentd-es gaf-zipkin"
    docker-compose up -d $LOAD_SERVICE
    LOAD_SERVICE="gaf-monitor gaf-cadvisor gaf-node-exporter gaf-prometheus gaf-grafana"
    docker-compose up -d $LOAD_SERVICE
    #修改某些挂载卷的权限
    edit_vol_permission
}

boot() {
    #设置工作目录
    workspace
    #检查命令
    check_commands docker docker-compose
    #开启防火墙规则
    port_gaf
    #创建docker网络
    create_docker_network gaf-net
    #创建挂载卷
    mkdir -p ${GAF_VOL_DIR}
    #拷贝挂载数据
    cp -rf $Root_Current_Dir/conf/GAF_ENV_CONFIG.env ${GAF_VOL_DIR}
    #替换GAF_ENV_CONFIG.env里的变量
    sed_config_env
    #启动GAF基础环境数据存储应用
    LOAD_SERVICE="gaf-postgres gaf-redis gaf-minio gaf-s3fs-mount-forboot"
    docker-compose up -d $LOAD_SERVICE
    #向postgres数据库导入基础数据
    wait_gaf_db
    source $Root_Current_Dir/conf/GAF_ENV_CONFIG.env
    docker run --rm --net=gaf-net -v $GAF_BASE_DATA_PATH:/opt/liquibase-data registry.cn-hangzhou.aliyuncs.com/supermap-gaf/build-tools:v1.0 \
        liquibase \
          --driver=$GAF_ENV_DATASOURCE_DRIVER \
          --classpath=/usr/local/liquibase/liquibase-classpath/postgresql-42.2.23.jar \
          --url=$GAF_ENV_DATASOURCE_URL \
          --username=$GAF_ENV_DATASOURCE_USERNAME \
          --password=$GAF_ENV_DATASOURCE_PASSWORD \
          --changeLogFile=liquibase-data/entry/gaf-boot.xml \
          update
    #启动GAF-BOOT应用
    docker-compose up -d gaf-boot
    #提示
    echo "启动GAF成功！！！GAF地址：http://${HOSTIP}"
}


#一键删除所有GAF应用及挂载
delete-all() {
    #设置工作目录
    workspace
    #关闭容器，删除容器
    docker-compose down
    #删除挂载
    rm -rf ${GAF_VOL_DIR}
}

#命令判断
case "$1" in
"help")
	usage
;;
"base")
    base
;;
"monitor")
    monitor
;;
"boot")
    boot
;;
"delete-all")
    delete-all
;;
*)
	usage
;;
esac


