#!/bin/bash

# commands "npm,cnpm,mvn,java" must setted in shell path
commands=(npm cnpm mvn java)
for command in ${commands[@]};
do
if ! type $command 2>/dev/null; then
	echo "$command not setted in shell path,failed!"
	exit 1
fi
done

BUILD_FOLDER=$(cd `dirname $0`; pwd)
DASHBOARD_PATH=$BUILD_FOLDER/../../dashboard
WEB_SOURCE_PATH=$DASHBOARD_PATH/src/main/webapp
WAR_PATH=$DASHBOARD_PATH/target/dashboard.war
TARGET_WAR_PATH=$BUILD_FOLDER/dashboard.war

cd $WEB_SOURCE_PATH
cnpm install
if [ $? -ne 0 ];then
    echo "install project dependency failed!"
    exit 1
fi
npm run build
if [ $? -ne 0 ];then
    echo "npm build failed!"
    exit 1
fi
rm -rf node_modules
cd $DASHBOARD_PATH
mvn package
cd $BUILD_FOLDER
mv $WAR_PATH $TARGET_WAR_PATH
