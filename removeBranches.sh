#!/bin/bash

branches=(

)

for i in "${branches[@]}"
do
	git branch -D $i
done

exit 0