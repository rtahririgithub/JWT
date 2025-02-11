# infra-paas-springboot-starter-project version 2

The PaaS Springboot Starter Project is intended to be a mechanism of jump starting TELUS teams that want to use OpenShift pipelines to deploy their applications quickly. Version 2 of this starter project takes advantage of the initial starter kit, but makes a series of improvements intended to allow new iterations of the starter-kit to be deployed with less effort that v1.

## Selected Components

The OpenShift project team took the introduction of this CI/CD pipeline as a mechanism to introduce some new technologies to TELUS. The hope is that the selected components can help developer productivity, while not introducing too much of a learning curve.

+ [SpringBoot 1.5](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-1.5-Release-Notes): Taking advantage of developer familiarity with TELUS Frameworks and Spring, SpringBoot was the natural choice to build microservice like architectures for Openshift.
+ [Jenkins 2.x](https://jenkins.io/changelog/#v2.60): Jenkins 2.x supports Jenkinsfile which allows us to adopt a "build as code" concept.  Jenkins files are auditable, reviewable and editable by all members of the team.
+ [Karate 0.5](https://github.com/intuit/karate#karate): The original candidate teams for our OpenShift pipeline are developing RESTful services. Intuit's recently open sourced Karate framework  was selected because it treats JSON as a first class citizen.

## Initial Setup

### Environment Management Approval
Project teams must work with the EnvironmentManagement team to judge the suitability of your application to the OpenShift environment. Please review the onboarding guides at [go/paas](https://jam2.sapjam.com/groups/FPc32ywZmowyH08hEHoJ93/overview_page/MPgctEap4MHd1thuYl2u2E).

### SCM Setup
Following EM approval to accept your application to OpenShift, the project team will be required to have an empty Git repository created in the SCM managed Git. Due to the nature of this effort, the application doesn't follow the traditional TELUS archetypes.

#### JIRA user
The user `PAASCI API USER` must be added to your Jira board. The pipeline uses this UID to post the results of integration tests and unit tests to JIRA.

### Starter Clone
The infra-paas-springboot-starter-project will be cloned into your new repository. The following scripts need to executed to create and initialize your OpenShift project

Version 2 of the pipeline re-organizes the management of build meta data. As such, the following attributes must be known at project creation:

- `application-name`:The OpenShift application name.
- `namepspace`: The OpenShift project name.
- `version`: The version number of the service or application.
- `git-repo`: The repository that contains the application source code.
- `git-branch`: The branch name to execute builds on.
- `jira-key`: The jira project to record issues against.
- `jira-epic-key`: The jira epic to record issues against.


The following scripts must be executed:
+ `/scripts/create-project-jenkins.sh`: Creates an OpenShift project and Jenkins instance.
+ `/scripts/create-branch-build.sh`: Links the Jenkins instance, OpenShift project and Git repository together. It also sets the various variables within the build configuration of the pipeline.

### Upstream repositories

In order to minimize the effort required to propagate changes from `infra-paas-springboot-starter-project` to derived projects, version 2 introduces the use of forks. A series of upstream repositories must be set in any local repositories:

```shell
git remote add upstream http://tfs.tsl.telus.com/tfs/telus/BT-GIT/_git/infra-paas-springboot-starter-project
```

Executing the command `git remote -v` should provide references to your application repository, as well as the starter project.

```shell
upstream http://tfs.tsl.telus.com/tfs/telus/BT-GIT/_git/infra-paas-springboot-starter-project (fetch)
upstream http://tfs.tsl.telus.com/tfs/telus/BT-GIT/_git/infra-paas-springboot-starter-project (push)
```

As changes are introduced by the PaaS team, updates can be retrieved from the parent project by executing the following steps.

+ Fetch from the upstream repository

```bash
git fetch upstream
```
+ Merge the changes to your master branch.

```bash
git merge upstream/master
```
+ If you encounter errors you may be required to invoke the command with the following arguments

```bash
git merge upstream/master --allow-unrelated-histories
```


### Pom.xml Customization
The GAV section of the pom.xml file must be modified to reflect the particulars of your application.
```Java
<groupId>telus.infra.paas</groupId>
<artifactId>paas-springboot-starter</artifactId>
<version>0.0.1-SNAPSHOT</version>
```

The properties section of the pom.xml file must be modified to reflect the CMDB ID of your application.

```Java
<properties>
  <cmdbAppID>19005</cmdbAppID>
</properties>
```

### Jenkinsfile Customization
Version 2 of the starter-kit minimizes the number of attributes that need to be managed in the Jenkinsfile. The only attribute that is likely to be managed is the Jenkins library that it includes.

```Java
@Library ('infra-paas-starter-lib@v2.0.1')
```

## Application Modification
Application teams are expected to refactor the `src/main` and `src/test` packages to implement their required business functionality as well as remove any template code . Additionally, the pipeline demands that a series of integration tests are written for the service. These must be completed in the `integration-test/src/test` directory.

### Appliction Configuration
The Paas Springboot Starter Project uses SpringBoot's native configuration capabilities. The file is located at `/etc/application.yaml` and requires spring profiles be established for all environments that the application is being deployed to. Accepted values are:
+ dv
+ it01
+ it02
+ it03
+ it04
+ pr

In order to support resource filtering in SpringBoot, a series of properties are reflected using the
`property=@foo@` notation. These are replaced via the following plugin during the build phase:

```Java
<resources>
  <resource>
      <directory>etc</directory>
      <filtering>true</filtering>
  </resource>
</resources>
```

### Context Root
Particular care must be exercised with the context root of the application. It must be set identically in `application.yaml` configuration and the pipeline build config as established when `create-branch-build.sh` was executed.

`etc/application.yaml`
```Java
server:
  contextPath: /v1
```

`oc get bc/master-app-v2-dev-pipeline -o yaml`
```yaml
strategy:
    jenkinsPipelineStrategy:
      env:
        - name: VERSION
          value: v2
```


Finally the controllers must honour the context root established in `etc/application.yaml`.

** This is required in order to allow the health check from Spring Actuator to function correctly with OpenShift **

### Notification

The file `notification.yaml` must be updated to reflect which individuals must be updated to receive notifications from OpenShift

```Java
all:
  - user1.name@telus.com
  - user2.name@telus.com
stage:
  - user3.name@telus.com
input:
  - user5.name@telus.com
error:
  - user4.name@telus.com
  - user5.name@telus.com
```
In the example provided, user1 and user2 will receive all notifications from OpenShift, but user3,user4,user5 will receive only select communications.

## SpringBoot Actuator Endpoints

The SpringBoot Starter project is configured to support [SpringBoot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/production-ready-endpoints.html).
When deployed to OpenShift, the `/health` endpoint is used to establish the health of the application. Other significant endpoints include:

+ `/health` Establish the health of the application
+ `/info` Get details about the build and tag of the application
+ `/env` Get details the environment Variables
+ `/metrics` Show metric information for the application


## Running Locally

While not encouraged, the springboot uber jar can be run locally without deploying to OpenShift. This will be useful to confirm that the application compiles, or for additional debugging purposes. The bash script demonstrates how the application could be executed. Due to the introduction of placeholder variables in application.yaml, the application.yaml that is created in the build and packaged `BOOT-INFO/classes/application.yaml` must be used instead of referencing a file with the `property=@foo@` notation at `/etc`.

### Compile

You must pass the argument `info.tag` to maven at compile time. This argument is typically set during execution of the Jenkinsfile and the uber jar will fail to run without it.

```java
mvn package -Dinfo.tag=local
```

### Run

The uber jar can be run via the following shell script.

```bash
#!/bin/sh
SPRINGBOOT_HOME=/home/kduffy/src/paas-springboot-starter-project
java -jar $SPRINGBOOT_HOME/target/paas-springboot-starter-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=local
```
