#!/bin/bash

if [ "$1" != "" ]; then
	_CP=.
	_CP=$_CP:$GROOVY_HOME/lib/groovy-2.4.3.jar
	_CP=$_CP:../websocket
	_CMD="groovy -cp $_CP $*"
	echo "_CMD=$_CMD"
	$_CMD
else
	echo "$0 <groovy script>"
fi

