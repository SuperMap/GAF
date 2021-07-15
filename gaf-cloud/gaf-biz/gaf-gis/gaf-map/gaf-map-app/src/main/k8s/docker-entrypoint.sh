#!/bin/bash
export JAVA_OPTS=`sed -e 's/^"//' -e 's/"$//' <<< "$JAVA_OPTS"`
sh -c /opt/init.sh
exec java $JAVA_OPTS -jar app.jar --spring.profiles.active=prod "$@"
