#!/bin/sh

sh -c /opt/init.sh
exec java ${JAVA_OPTS} -jar app.jar --spring.profiles.active=prod "$@"
