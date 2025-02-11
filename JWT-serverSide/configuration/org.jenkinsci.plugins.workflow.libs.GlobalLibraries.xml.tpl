<?xml version='1.0' encoding='UTF-8'?>
<org.jenkinsci.plugins.workflow.libs.GlobalLibraries plugin="workflow-cps-global-lib@2.5">
  <libraries>
    <org.jenkinsci.plugins.workflow.libs.LibraryConfiguration>
      <name>infra-paas-starter-lib</name>
      <retriever class="org.jenkinsci.plugins.workflow.libs.SCMSourceRetriever">
        <scm class="jenkins.plugins.git.GitSCMSource" plugin="git@3.1.0">
          <id>dcf90cfe-f00f-42e1-8126-e60ece288cd5</id>
          <remote>http://tfs.tsl.telus.com/tfs/telus/BT-GIT/_git/infra-paas-starter-lib</remote>           <credentialsId>${OPENSHIFT_BUILD_NAMESPACE}-telus-git</credentialsId>
          <remoteName>origin</remoteName>
          <rawRefSpecs>+refs/heads/*:refs/remotes/origin/*</rawRefSpecs>
          <includes>*</includes>
          <excludes></excludes>
          <ignoreOnPushNotifications>false</ignoreOnPushNotifications>
        </scm>
      </retriever>
      <implicit>false</implicit>
      <allowVersionOverride>true</allowVersionOverride>
    </org.jenkinsci.plugins.workflow.libs.LibraryConfiguration>
  </libraries>
</org.jenkinsci.plugins.workflow.libs.GlobalLibraries>
