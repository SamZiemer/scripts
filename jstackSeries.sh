#!/bin/bash
if [ $# -eq 0 ]; then
	pid=$(jps | grep -oP '\d+ Bootstrap' | grep -oP '\d+')
	user=${whoami}
	count=${3:-10}
	delay=${4:-0.5}

elif [ $1 == '--help' ] || [ $1 == '-h' ]; then
	echo "Usage: jstackSeries <pid> <run_user> [ <count> [ <delay> ] ]"
	echo "    Defaults: count = 10, delay = 0.5 (seconds)"
	echo "If no arguments are passed, pid and run_user will be found automatically"
	exit 0
else
pid=$1			# required
user=$2			# required
count=${3:-10}	# defaults to 10 times
delay=${4:-0.5}	# defaults to 0.5 seconds

fi
if [ $1 !='--help' ] && [ $1 !='-h' ];
then
	while [ $count -gt 0 ]
	do
		jstack -l $pid > threadDump.$pid.$(date +%H%M%S)
		sleep $delay
		let count--
		echo -n "."
	done
fi