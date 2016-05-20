#!/bin/bash

start=$(date +"%s")

now=$(date)
echo $now

mysql -u root -ppassword < ~/scripts/61restore.sql

end=$(date +"%s")

runtime=$(($end-$start))

echo -e "\n\nRuntime took $(($runtime/60)) minutes and $(($runtime%60)) seconds."