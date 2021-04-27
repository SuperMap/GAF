#!/usr/bin/env bash

workspace() {
    #设置工作目录
    export Root_Current_Dir=$(cd `dirname $0`;pwd)
    cd $Root_Current_Dir
    #命令文件权限
    find $Root_Current_Dir -name "*.sh"|xargs chmod +x
    #加载GAF函数
    source $Root_Current_Dir/bin/gaf-func.sh
}

build() {
    build_frontend
    cd $Root_Current_Dir/../../../
    build_images
}


workspace
build