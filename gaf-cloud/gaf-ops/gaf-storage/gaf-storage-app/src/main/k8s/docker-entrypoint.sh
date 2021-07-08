#!/bin/sh

exec java ${JAVA_OPTS} -jar app.jar --spring.profiles.active=prod "$@"