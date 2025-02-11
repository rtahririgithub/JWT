#!/bin/bash

if [ $# -lt 8 ]; then
    echo "Usage: create-branch-build.sh <application-name> <git-repo> " \
    "<git-branch> <namespace> <version> <jira-key> " \
    "<jira-epic-key> <enable-static-code-analysis> " \
    "<datacenter>"
    exit
fi

oc process -f ../openshift/build-template.yaml -p APPLICATION_NAME=$1 \
  -p GIT_URI=$2 -p GIT_REF=$3 -p PROJECT_NAMESPACE=$4 -p VERSION=$5 \
  -p JIRA_KEY=$6 -p JIRA_EPIC_KEY=$7  \
  -p ENABLE_STATIC_CODE_ANALYSIS=$8 \
  -p DATACENTER=$9 | oc create -n $4 -f -
