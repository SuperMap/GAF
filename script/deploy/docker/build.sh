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
    build_frontend
    cd $Root_Current_Dir/../../../
    build_images
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
"push")
    workspace
    push
;;
*)
	workspace
    build
;;
esac

