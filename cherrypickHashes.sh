#!/bin/bash

hashes=(

)

for i in "${hashes[@]}"
do
	git cherry-pick $i
done

exit 0