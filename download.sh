#!/bin/bash

file=images.txt

while read line ; do
	target=$(echo "$line" | cut -d~ -f1).png
	source=$(echo "$line" | cut -d~ -f2)
	sourcefile=$(basename "$source")
	wget -v $source
	mv -f $sourcefile ./web/images/$target
done < ${file}