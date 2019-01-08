#!/bin/bash

files=(

)

for i in "${files[@]}"
do
	git checkout $i
done

exit 0