if [%4]==[] goto usage

oc process -f ..\openshift\build-template.yaml -p APPLICATION_NAME=%1 -p GIT_URI=%2 -p GIT_REF=%3 | oc create -n %4 -f -
goto :eof

:usage
@echo "Usage: create-branch-build.sh <application-name> <git-repo> <git-branch> <namespace>"
exit /B 1

