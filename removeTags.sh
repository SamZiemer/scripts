#!/bin/bash

tags=(
	
)

for i in "${tags[@]}"
do
	git tag -d $i
done

exit 0