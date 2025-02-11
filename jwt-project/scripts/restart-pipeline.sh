#!/bin/bash

if [ $# -lt 2 ]; then
    echo "Usage: restart-pipeline.sh <Pipeline Name> <Namespace> " 
    exit
fi

BUILD=`oc get build --ignore-not-found -n $2 -l buildconfig=$1|grep Running | cut -d' ' -f1`


if [ "$BUILD" != "" ]; then
  echo "Restarting build: $BUILD"
  oc cancel-build $BUILD -n $2
  sleep 5
  oc start-build $1
else
  echo "No running build for pipeline $1 found."
fi
