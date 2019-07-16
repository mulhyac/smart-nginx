#!/bin/bash
cd  ${workspace}
rm -rf ${workspace}/${cloneName}
git clone ${git}
#暂时不处理日志，后期需要将日志通过websocket显示到前端页面
cd ${workspace}/${cloneName}
<#if javaHome??>
export JAVA_HOME=${javaHome}
export PATH=$JAVA_HOME/bin:$PATH
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
</#if>
<#if mavenHome??>
export MAVEN_HOME=${mavenHome}
export PATH=$MAVEN_HOME/bin:$PATH
</#if>
mvn clean install -X -Dmaven.test.skip=true