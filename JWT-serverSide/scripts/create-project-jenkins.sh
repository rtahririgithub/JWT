#!/bin/bash

if [ $# -lt 3 ]; then
    echo "Usage: create-project-jenkins.sh <git-repo> <git-branch> <namespace>"
    exit
fi

oc new-build openshift/jenkins-2-centos7:v3.6~http://svc_devsvcwdc:Tab2016!@`echo $1 | cut -d/ -f3-`#$2 --name=jenkins -n $3
oc new-app jenkins-ephemeral \
    -p NAMESPACE=$3 \
    -p JENKINS_IMAGE_STREAM_TAG=jenkins:latest -n $3
sleep 2

#add image build to jenkins user to be able to run oc build
oc policy add-role-to-user system:image-builder jenkins -n $3
