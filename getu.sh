#!/bin/bash

printHelpAndExit() {
	echo ''
	echo 'Get Liferay Github Usernames'
	echo ''
	echo 'Usage:'
	echo '    getu [-bfhu] <search terms>'
	echo ''
	echo 'Options: (all optional)'
	echo '    -b : Open the Github profile of the first user in a browser'
	echo '    -f : Display only full names'
	echo '    -h : Display this help message'
	echo '    -u : Display only usernames'
	echo ''
	echo 'Example:'
	echo '    getu -u brian'
	echo ''
	exit "$1"
}

OPEN_PROFILE=0
SHOW_FULLNAME=0
SHOW_USERNAME=0

while getopts bfhu FLAGS; do
	case $FLAGS in
		b) OPEN_PROFILE=1 ;;
		f) SHOW_FULLNAME=1 ; SHOW_USERNAME=0 ;;
		h) printHelpAndExit 0 ;;
		u) SHOW_FULLNAME=0 ; SHOW_USERNAME=1 ;;
		*) printHelpAndExit 1 ;;
	esac
done

shift $((OPTIND-1))

[ "$#" -gt 0 ] || printHelpAndExit 1

url="$(echo "https://github.com/orgs/liferay/people?query=${*}" | sed "s| |%20|g")"

# curl: gets html from results page
# 	tr: replaces all newlines with spaces
# 	sed: splits into multiple lines by pattern (OS X specific)
# 	grep: filters out irrelevant 1st line
# 	sed: flags extended regex
# 		1st -e: removes everything up till USERNAME
# 		2nd -e: replaces everything from USERNAME till FULLNAME with a colon
# 		3rd -e: removes everything after FULLNAME
# 		4th -e: replaces escaped double-quotes with actual double-quotes
# 		5th -e: swaps output so FULLNAME shows first
# 	sort: sorts them

RESULTS="$(
curl -s "${url}" |
	tr '\n' ' ' |
	sed 's| member-info|\'$'\n&|g' |
	grep member-info |
	sed -E \
		-e 's|^.*css-truncate-target f4" href="/||' \
		-e 's|"> +| : |' \
		-e 's| +</a>.*||' \
		-e 's|&quot;|"|g' \
		-e 's|(.*) : (.*)|\2 : \1|' |
	sort
)"

exists() {
	command -v "$1" >/dev/null 2>&1
}

openUrlInBrowser() {
	if exists open ; then
		open "$1" ; exit 0
	elif exists xdg-open ; then
		xdg-open "$1" ; exit 0
	elif exists python ; then
		python -m webbrowser "$1" ; exit 0
	elif exists start ; then
		start "$1" ; exit 0
	else
		echo "No program found to open URL \"$1\"" ; exit 1
	fi
}

if [ -z "${RESULTS}" ] ; then
	echo "No users found..." ; exit 1
elif [ ${OPEN_PROFILE} -gt 0 ] ; then
	openUrlInBrowser "https://github.com/$(echo "${RESULTS}" | head -n 1 | awk -F' : ' '{print $2}')"
elif [ ${SHOW_FULLNAME} -gt 0 ] ; then
	echo "${RESULTS}" | awk -F' : ' '{print $1}' ; exit 0
elif [ ${SHOW_USERNAME} -gt 0 ] ; then
	echo "${RESULTS}" | awk -F' : ' '{print $2}' ; exit 0
else
	echo "${RESULTS}" ; exit 0
fi