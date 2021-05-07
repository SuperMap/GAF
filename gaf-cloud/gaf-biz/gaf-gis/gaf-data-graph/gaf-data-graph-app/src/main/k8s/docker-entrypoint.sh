#!/bin/sh

exec java -jar app.jar --spring.profiles.active=prod "$@"
