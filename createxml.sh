#!/bin/bash

while read line; do
        echo -n '<package name="'
        echo -n $line
        echo '" withSubpackages="true" static="false" />'
        echo '<emptyLine />'
done