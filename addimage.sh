#!/bin/bash

if [ $# -ne 2 ]; then
	echo "wrong"
	exit 1
fi

var_file=$1
var_source=$2

echo $1~$2>>images.txt