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
	firewall-cmd --add-port=$1/tcp --permanent
	service firewalld restart
}


#构建打包所有GAF应用
build_all() {
    cd $Root_Current_Dir/../../../

    mvn clean package -Dmaven.test.skip=true

    cd gaf-web
    yarn cache clean
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
    yarn install --update-checksums
    yarn link common-gaf
    yarn link common-webbase
    yarn link common-mapapp
    yarn generate
    rm -rf node_modules
    cd ..

    cd gaf-mapapp
    yarn install --update-checksums
    yarn link common-gaf
    yarn link common-webbase
    yarn link common-mapapp
    yarn generate
    rm -rf node_modules

    cd $Root_Current_Dir
}

#修改某些挂载卷的权限
edit_vol_permission() {
    chmod -R 777 ${GAF_VOL_DIR}/vol_prometheus/data
    chmod -R 777 ${GAF_VOL_DIR}/vol_elasticsearch/data
}