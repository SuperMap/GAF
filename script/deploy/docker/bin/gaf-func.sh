#检查docker是否有对应容器名正在运行
check_container_exist(){
  local component=$1
  local container_name=$(docker ps --format "{{.Names}}"|grep -w $component)
  if [ -z "$container_name" ]; then
    echo 0
  else
    echo 1
  fi
}

#检查容器健康状态
check_container_health(){
    local health_json=`docker inspect --format '{{json .State.Health}}' $1`
    local health_pattern="\"Status\":\"healthy\""
    if [[ $health_json == *"$health_pattern"* ]]; then
        echo 0
    else
        echo 1
    fi
}

#等待容器健康状态正常
wait_container_health(){
    for i in $*; do
        local flag=0
        echo "wait for $i...";
        while [ $flag -eq 0 ]; do
            local check_result=$(check_container_health $i)
            if [ "$check_result" -eq 0 ]; then
                flag=1
            fi
        done
    done
}



#判断命令是否安装
check_commands(){
  commands=(docker docker-compose psql git java mvn yarn)
  commands_flag=""
  for command_name in ${commands[*]}; do
      if ! [ -x "$(command -v $command_name)" ]; then
          echo "检查命令是否安装[$command_name] : FALSE"
          commands_flag=$command_name
      else
          echo "检查命令是否安装[$command_name] : TRUE"
      fi
  done
  if [ -n "$commands_flag" ]; then
      echo "环境缺少：[$commands_flag]！"
      exit 1
  fi
}

#创建docker网络
create_docker_network(){
    set +e
    docker network create $1
    set -e
}

#等待数据库启动
wait_gaf_db(){
    bash $Root_Current_Dir/bin/wait-for-it.sh ${HOSTIP}:5432 --timeout=600 --strict --  echo "数据库准备就绪"
}
#加载db数据
load_db_data(){
    wait_gaf_db

    local restore_files=$(find $Root_Current_Dir/data/db  -type f |sort)
    
    local restore_file
    for restore_file in $restore_files; do
        local file_extension=$(basename $restore_file|cut -d . -f2)
        if [ "$file_extension" == "sql" ]; then
            load_gaf_sql $restore_file
        else
            dump_restore $restore_file
        fi
    done
}
#数据库备份件恢复
dump_restore(){
    local dump_file=$1
    local restore_db=$(basename $dump_file |cut -d . -f1)
    PGPASSWORD=123456 pg_restore -h $HOSTIP -p 5432  -U admin --no-owner -d $restore_db $dump_file
}
#gaf数据库执行sql文件
load_gaf_sql(){
    local sql_file=$1
    PGPASSWORD=123456 psql -o /dev/null -q -h $HOSTIP -p 5432 -U admin -d gaf -f $sql_file
}

#替换环境配置文件中ip变量
sed_config_env(){
    sed -i 's/${HOSTIP}/'$HOSTIP'/g' ${GAF_VOL_DIR}/GAF_ENV_CONFIG.env
}

#开启防火墙端口
port(){
    for i in $*; do
    	firewall-cmd --zone=public --add-port=$i/tcp --permanent
    done
	firewall-cmd --reload
}

#开启gaf服务的防火墙端口
port_gaf() {
    port 9000 30777
}

#构建打包所有GAF应用
build_frontend() {
    cd $Root_Current_Dir/../../../

    mvn clean package -Dmaven.test.skip=true

    cd gaf-web
    cd common-gaf
    yarn install --update-checksums
    yarn link
    cd ..

    cd common-webapp
    yarn install --update-checksums
    yarn link
    cd ..

    cd common-mapapp
    yarn install --update-checksums
    yarn link
    cd ..

    cd gaf-webapp
    yarn cache clean
    yarn install --update-checksums
    yarn link common-gaf
    yarn link common-webbase
    yarn link common-mapapp
    yarn generate
    rm -rf node_modules
    cd ..

    cd gaf-mapapp
    yarn cache clean
    yarn install --update-checksums
    yarn link common-gaf
    yarn link common-webbase
    yarn link common-mapapp
    yarn generate
    rm -rf node_modules

    cd $Root_Current_Dir
}

build_images() {
    mvn clean package dockerfile:build -Ddockerfile.build.skip -Dmaven.test.skip=true -DCUSTOM_REGISTRY=docker_ -DCUSTOM_TAG=latest
}

#修改某些挂载卷的权限
edit_vol_permission() {
    chmod -R 777 ${GAF_VOL_DIR}/vol_prometheus/data
    chmod -R 777 ${GAF_VOL_DIR}/vol_elasticsearch/data
}