#!/bin/bash
CUR_DIR=$(cd `dirname $0`; pwd)
INIT_JS_DIR=create_user.js
MONGODB_DATA_DIR=$CUR_DIR/mongodb_data

#mongodb bin dirctory must setted in shell PATH
#"MONGODB_DATA_HOME" vaiable must setted in shell enviroment
if [ -z $MONGODB_DATA_HOME ]; then
	echo "MONGODB_DATA_HOME path variable not setted in shell enviroment,failed!"  
	exit 1
fi
if ! type mongod 2>/dev/null; then
	echo "mongodb bin path not setted in shell path,failed!"
	exit 1
fi

#stop mongod if exists
if ps -ef | grep mongod | egrep -v grep >/dev/null 
then
    mongod --shutdown --dbpath=${MONGODB_DATA_HOME}/data/db
	sleep 2
fi
#stop mongod which not stated by ${MONGODB_DATA_HOME}/data/db path
if ps -ef | grep mongod | egrep -v grep >/dev/null 
then
	mongodpid=$(ps -ef | grep mongod | grep -v grep | awk '{print $2}')
	kill -9 $mongodpid;
	sleep 2
fi

#create mongodata dirctory
if [ -d "$MONGODB_DATA_HOME" ]; then  
	rm -rf $MONGODB_DATA_HOME
fi
mkdir -p ${MONGODB_DATA_HOME}/log
mkdir -p ${MONGODB_DATA_HOME}/data/db

#start mongod
mongod --fork --dbpath=${MONGODB_DATA_HOME}/data/db --logpath=${MONGODB_DATA_HOME}/log/mongodb.log
sleep 2

#import data
#import data
for file in `ls $MONGODB_DATA_DIR`
do
	if echo $file | grep -qE "*.json"
	then
		collectionName=${file%.*}
		mongoimport --db timedb --collection $collectionName --file $MONGODB_DATA_DIR/$collectionName.json
	fi
done

#create user
mongo --host localhost:27017 "$INIT_JS_DIR"
