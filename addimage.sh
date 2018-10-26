#!/bin/bash

if [ $# -ne 2 ]; then
	echo "wrong"
	exit 1
fi

var_file=$1.png
var_source=$2

var_updated=$(grep -v ^$1~ images.txt)
echo $var_updated>images.txt
echo $1~$2>>images.txt