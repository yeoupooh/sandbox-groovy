#!/bin/bash

if [ "$1" == "" ]; then
	echo "USAGE: $0 <groovy file>"
	exit
fi

CP=.
for f in libs/*.jar; 
	do echo "Processing $f file..";
	CP=$CP:$f
done
echo $CP

groovy -cp $CP $1

