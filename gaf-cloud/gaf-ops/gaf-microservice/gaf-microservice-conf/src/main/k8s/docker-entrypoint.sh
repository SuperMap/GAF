#!/bin/bash
export JAVA_OPTS=`sed -e 's/^"//' -e 's/"$//' <<< "$JAVA_OPTS"`
exec java $JAVA_OPTS -jar app.jar "$@"