#!/bin/bash
if [ $# -eq 0 ]; then
    echo "Useage: checkUpgradeDiffs.sh <oldTag> <newTag>"
    echo "    Outputs differences between the two tags to ~/Documents/CheckUpgradeProcesses.diff"
    exit 1
fi

if [ -f ~/Documents/CheckUpgradeProcesses.diff ]; then
	TODAY=`date '+%Y%m%d'`;
	mv ~/Documents/CheckUpgradeProcesses.diff ~/Documents/CheckUpgradeProcesses.$TODAY.diff
fi

git ls-files portal-impl | grep upgrade | grep -v Test | xargs git diff $1..$2 -- > ~/Documents/CheckUpgradeProcesses.diff