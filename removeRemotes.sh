#!/bin/bash

branches=(

)

for i in "${branches[@]}"
do
	git branch -rd $i
done

exit 0