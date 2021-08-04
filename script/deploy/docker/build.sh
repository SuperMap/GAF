#!/usr/bin/env bash


#使用说明
usage()
{
    cat << USAGE >&2
Usage:
	GAF应用构建脚本 build [COMMANDS]

	COMMANDS:

	build                       --调用容器执行构建命令(需要docker环境)
	build_in_container          --执行构建命令(需要构建环境)
	boot                        --调用容器执行GAF-BOOT构建命令(需要docker环境)
	boot_in_container           --执行GAF-BOOT构建命令(需要构建环境)
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
}

build_in_container() {
    #检查命令
    check_commands docker java mvn yarn
    #构建前端产出dist目录
    build_frontend
    cd $Root_Current_Dir/../../../
    #构建前端、后端镜像
    build_images
}

build() {
    #检查命令
    check_commands docker
    #寻找项目路径，挂载点
    export Gaf_Project_Path=`readlink -f $Root_Current_Dir/../../../`
    check_gaf_project_exist
    #创建docker容器，容器内进行编译打包镜像
    docker run --rm -v /var/run/docker.sock:/var/run/docker.sock -v $Gaf_Project_Path:/opt/GAF registry.cn-hangzhou.aliyuncs.com/supermap-gaf/build-tools:v1.0 bash /opt/GAF/script/deploy/docker/build.sh build_in_container
}

boot_in_container() {
    #检查命令
    check_commands docker java mvn yarn
    #构建前端产出dist目录
    build_frontend
    #移动dist目录
    cd $Root_Current_Dir/../../../
    cp -r gaf-web/gaf-webapp/dist gaf-boot/dist_gaf-webapp
    cp -r gaf-web/gaf-mapapp/dist gaf-boot/dist_gaf-mapapp
    #构建gaf-boot镜像
    mvn clean package dockerfile:build -Ddockerfile.build.skip -Dmaven.test.skip=true -DCUSTOM_REGISTRY=${GAF_REGISTRY} -DCUSTOM_TAG=${GAF_REGISTRY_TAG} -pl gaf-boot -am
}

boot() {
    #检查命令
    check_commands docker
    #寻找项目路径，挂载点
    export Gaf_Project_Path=`readlink -f $Root_Current_Dir/../../../`
    check_gaf_project_exist
    #创建docker容器，容器内进行编译打包镜像
    docker run --rm -v /var/run/docker.sock:/var/run/docker.sock -v $Gaf_Project_Path:/opt/GAF registry.cn-hangzhou.aliyuncs.com/supermap-gaf/build-tools:v1.0 bash /opt/GAF/script/deploy/docker/build.sh boot_in_container
}


#命令判断
case "$1" in
"help")
	usage
;;
"build")
    workspace
    build
;;
"build_in_container")
    workspace
    build_in_container
;;
"boot")
    workspace
    boot
;;
"boot_in_container")
    workspace
    boot_in_container
;;
*)
	workspace
	build
;;
esac
