#!/bin/bash

#启动前端项目
service nginx start
#启动后端项目
sh -c /opt/init.sh
exec java -jar /app.jar --spring.profiles.active=docker "$@"

