#!/bin/sh

sh -c /opt/init.sh
exec java -jar app.jar --spring.profiles.active=docker "$@"
