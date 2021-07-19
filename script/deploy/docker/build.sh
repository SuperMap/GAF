#!/usr/bin/env bash


#使用说明
usage() {
	echo "Usage:	./deploy COMMAND"
	echo ""
	echo "【GAF应用部署程序】"
	echo ""
	echo "Commands:"
	echo "  help          --查看帮助"
	echo "  push          --构建镜像并推送镜像到镜像仓库(需要在Maven配置文件设置仓库账号信息)"
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

build() {
     #检查命令
    check_commands docker java mvn yarn
    #构建前端产出dist目录
    build_frontend
    cd $Root_Current_Dir/../../../
    #构建前端、后端镜像
    build_images
}

container_build() {
    #检查命令
    check_commands docker
    #寻找项目路径，挂载点
    export Gaf_Project_Path=`readlink -f $Root_Current_Dir/../../../`
    check_gaf_project_exist
    #创建docker容器，容器内进行编译打包镜像
    docker run --rm -v /var/run/docker.sock:/var/run/docker.sock -v $Gaf_Project_Path:/opt/GAF registry.cn-hangzhou.aliyuncs.com/supermap-gaf/build-tools:v1.0 bash /opt/GAF/script/deploy/docker/build.sh build
}


push() {
    build_frontend
    cd $Root_Current_Dir/../../../
    build_push_images
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
*)
	workspace
	container_build
;;
esac

