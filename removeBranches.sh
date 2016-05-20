#!/bin/bash

branches=(
	
)

for i in "${branches[@]}"
do
	git branch -Dr $i
done

exit 0