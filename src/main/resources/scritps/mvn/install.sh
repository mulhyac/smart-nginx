#!/bin/bash
cd  ${workspace}
rm -rf ${workspace}${separator}${cloneName}
git clone ${git}
#暂时不处理日志，后期需要将日志通过websocket显示到前端页面
cd ${workspace}${separator}${cloneName}
<#if mavenHome??>
export MAVEN_HOME=${mavenHome}
export PATH=$MAVEN_HOME/bin:$PATH
</#if>
mvn clean install -X -Dmaven.test.skip=true