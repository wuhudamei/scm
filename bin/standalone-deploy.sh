#!/bin/bash
NAME=scm

cd `dirname $0`
cd ..
PROJECT_DIR=`pwd`
AID=scm-.+-standalone.war
DEPLOY_DIR=/web_jenkins/$NAME
WORK_DIR=/web_jenkins/$NAME/work

PID=`ps -ef | grep -E  "$AID" | grep -v grep |awk '{print $2}'`
if [ -n "$PID" ]; then
    kill -9 $PID
    echo "$PID killed"
fi

if [ ! -d $DEPLOY_DIR ]; then
    mkdir -v $DEPLOY_DIR
fi
rm -rfv $DEPLOY_DIR/*.war
if [ -d $WORK_DIR ]; then
    rm -rfv $WORK_DIR
fi
WAR=`ls target | grep standalone.war`
cp -v target/$WAR $DEPLOY_DIR

cd $DEPLOY_DIR
echo starting..
nohup java -jar $WAR >$NAME.log 2>&1 &
echo DONE