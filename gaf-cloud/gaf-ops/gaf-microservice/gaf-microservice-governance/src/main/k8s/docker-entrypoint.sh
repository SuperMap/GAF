#!/bin/bash

exec java ${JAVA_OPTS:1:-1} -jar app.jar --spring.profiles.active=prod "$@"