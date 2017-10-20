ANT_OPTS="-Xmx8192m -XX:MaxPermSize=2048m"
JAVA_HOME="/opt/java/jdk1.8.0_144"
JRE_HOME="/opt/java/jdk1.8.0_144/jre"
PATH=$PATH:~/scripts/:$JAVA_HOME/bin:/home/ziemers/miniconda3/bin
export ANT_OPTS PATH JAVA_HOME JRE_HOME

GITHUB_USERNAMES="adolfopa, arboliveira, brandizzi, danielkocsis, drewbrokke, dustinryerson, ealonso, epgarcia, hhuijser, holatuwol, igorspasic, johnnyhowey, jonmak08, jorgeferrer, juliocamarero, marcellustavares, matethurzo, mhan810, pei-jung, rotty3000, sergiogonzalez, shinnlok, shuyangzhou, topolik, zoltantakacs"

OAUTHTOKEN="9271984a0bcb122e0a0c7ff562feb1b6c5403ee4"

## ALIASES ##
##alias aa='ant all'
alias ad='ant deploy'
alias adf='ant deploy-fast'
alias afs='ant format-source-current-branch'
alias clean='git checkout .; git clean -fd;'
alias dbg="clear && printf '\e[3J'; ./catalina.sh jpda run;"
alias dsr='find . -name '.DS_Store' -type f -delete'
alias git="hub"
alias gbv="git bisect visualize"
alias gc="git checkout"
alias gcp="git cherry-pick"
alias gcpc="git cherry-pick --continue"
alias gfo='git fetch origin'
alias gfu='git fetch upstream'
alias gitfullclean='git clean -xfd -e "*.iml" -e "*ziemers*" -e "*.idea/"'
alias gmt="git mergetool"
alias gl='git stash list'
alias gs='git status'
alias heapdump="jps | grep -oP '\d+ Bootstrap' | grep -oP '\d+' | xargs jmap -dump:format=b,file=dump.bin"
alias ignored='git ls-files -v | grep ^[a-z]'
alias java7="source java7.sh"
alias java8="source java8.sh"
alias jca="java -jar ~/Applications/jca/jca.jar"
alias kazissh="ssh sam@jktest.hopto.org"
alias kb="jps | grep -oP '\d+ Bootstrap' | grep -oP '\d+' | xargs kill -9"
alias mysqlp='mysql -uroot -ppassword'
alias run="clear; ./catalina.sh run;"
alias s61="cd /home/ziemers/servers/liferay-portal-ee-6.1.x/tomcat-7.0.42/bin/"
alias s62="cd /home/ziemers/servers/liferay-portal-ee-6.2.x/tomcat-7.0.42/bin/"
alias see="cd /home/ziemers/servers/liferay-portal-ee/tomcat-8.0.32/bin/"
alias smaster="cd /home/ziemers/servers/liferay-portal/tomcat-8.0.32/bin/"
alias sp3="git checkout fix-pack-base-6130-sp3"
alias sp7="git checkout fix-pack-base-6210-sp7"
alias sp10="git checkout fix-pack-base-6210-sp10"
alias sp13="git checkout fix-pack-base-6210-sp13"
alias sp15="git checkout fix-pack-base-6210-sp15"
alias sp20="git checkout fix-pack-base-6210-sp20"
alias r61="cd /home/ziemers/repos/liferay-portal-ee-6.1.x"
alias r62="cd /home/ziemers/repos/liferay-portal-ee-6.2.x"
alias r70x="cd /home/ziemers/repos/liferay-portal-ee-7.0.x"
alias r70xp="cd /home/ziemers/repos/liferay-portal-ee-7.0.x-private"
alias rmaster="cd /home/ziemers/repos/liferay-portal"
alias ree="cd /home/ziemers/repos/liferay-portal-ee"
alias refreshbash="source ~/.bashrc"
alias bsp7="cd ~/bundles/liferay-portal-6.2.10-sp7/tomcat-7.0.42/bin/"
alias b62="cd ~/bundles/liferay-portal-6.2.10/tomcat-7.0.42/bin/"
alias b6120="cd ~/bundles/liferay-portal-6.1.20/tomcat-7.0.27/bin/"
alias taketd="jstackSeries.sh"
alias td="jps | grep -oP '\d+ Bootstrap' | grep -oP '\d+' | xargs jstack"
alias tdu="jps | grep -oP '\d+ DBUpgraderLauncher' | grep -oP '\d+' | xargs jstack"
alias upgrade='clear && printf "\e[3J"; java -jar com.liferay.portal.tools.db.upgrade.client.jar -j="-Xmx4096m"'
alias upgradedbg='clear && printf "\e[3J"; java -jar com.liferay.portal.tools.db.upgrade.client.jar -j="-Xmx4096m -Dfile.encoding=UTF8 -Duser.country=US -Duser.language=en -Duser.timezone=GMT -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000"'

alias ss='show_stash'
alias as='apply_stash'
alias gmu='merge_upstream'

############# FUNCTIONS BEGIN ################

MCD_RD_CLONE_PATH=/home/ziemers/repos/liferay-faster-deploy

function subrepobp() {
        SUBREPO_ROOT=/home/ziemers/repos \
                ${MCD_RD_CLONE_PATH}/patcher/subrepobp $@
}

function aa() {
	RNAME=${PWD##/*/}

	if [[ "$RNAME" == liferay-portal ]]
	then
		${MCD_RD_CLONE_PATH}/nodejs/safeant all
	elif [[ "$RNAME" == liferay-portal-ee ]]
	then
		${MCD_RD_CLONE_PATH}/nodejs/safeant all
	elif [[ "$RNAME" == liferay-portal-ee-7.0.x ]]
	then
		${MCD_RD_CLONE_PATH}/nodejs/safeant all
	elif [[ "$RNAME" == liferay-portal-ee-6.1.x ]]
	then
		ant all
	elif [[ "$RNAME" == liferay-portal-ee-6.2.x ]]
	then
		ant all
	elif [[ "$RNAME" == *liferay-plugins-ee* ]]
	then
		ant all
	elif [[ "$RNAME" == *-private* ]]
	then
		ant all
	else
		echo "Not a build repo"
	fi
}

function gpr() {
	SUBREPO_ROOT=/home/ziemers/repos/ \
		${MCD_RD_CLONE_PATH}/github/pullrequest $@
}

function dump () {
	if [ "$1" == "" ]; then
		echo "useage dump <dbNameToDump> <nameOfFileToBackUpTo>"
	elif [ "$2" == "" ]; then
		echo "useage dump <dbNameToDump> <nameOfFileToBackUpTo>"
	else
		if [ -f ~/backups/$2 ]; then
			rm -f ~/backups/$2
		fi

		echo "drop database $1;" >> ~/backups/$2
		echo "create database $1 character set utf8;" >> ~/backups/$2
		echo "use $1;" >> ~/backups/$2
		mysqldump -u root -ppassword $1 >> ~/backups/$2
	fi
}

function newdb () {
	if [ "$1" == "" ]; then
		echo "Please enter a database name to drop and create"
	fi
	mysqlp -e "drop database $1;"
	mysqlp -e "create database $1 character set utf8;"
}

function show_stash () {
	git stash show -p stash@{$1}
}

function apply_stash () {
	git stash apply stash@{$1}
}

function merge_upstream () {
	git merge upstream/$1
}

function gft () {
	git fetch --no-tags upstream tags/$1:refs/tags/$1
}

function gpo () {
	BRANCH_NAME=$(git branch | awk '/\*/ { print $2; }')
	git push origin "$BRANCH_NAME"
}

gw() {
	if [ -f ../settings.gradle ]; then
		echo rm ../settings.gradle
		rm ../settings.gradle
		git update-index --assume-unchanged ../settings.gradle
	fi

	$(git rev-parse --show-toplevel)/gradlew $@
}

gcd() {
	gw clean deploy
}

gd() {
	gw deploy
}

gfs() {
	gw formatSource
}

gau() {
	git update-index --assume-unchanged $1
}

#####################################
############ MCD UTILS ##############
#####################################

## AWS ##

ec2() {
	if [ "" != "$EC2_HOST" ]; then
		/usr/bin/ssh -i $HOME/.ssh/mdang-training-shared.pem ubuntu@$EC2_HOST
	else
		echo set EC2_HOST$HOME/.ssh/mdang-training-shared.pem
	fi
}

ec2down() {
	if [ "" != "$EC2_HOST" ]; then
		TARGET_FILE=`basename $1`
		/usr/bin/scp -i $HOME/.ssh/mdang-training-shared.pem ubuntu@$EC2_HOST:$1 $TARGET_FILE
	else
		echo set EC2_HOST
	fi
}

ec2up() {
	if [ "" != "$EC2_HOST" ]; then
		/usr/bin/scp -i $HOME/.ssh/mdang-training-shared.pem $1 ubuntu@$EC2_HOST:~/$1
	else
		echo set EC2_HOST
	fi
}

## end AWS ##


IJ_CLONE_PATH=/home/ziemers/trainings/liferay-intellij

ij() {
        ${IJ_CLONE_PATH}/intellij "$@"
}


## CD changes ##

cd() {
        . /home/ziemers/repos/liferay-faster-deploy/gitcd/gitcd $@
}

cdb() {
        . /home/ziemers/repos/liferay-faster-deploy/gitcd/gitcdb $@
}

cdp() {
        . /home/ziemers/repos/liferay-faster-deploy/gitcd/gitcdp $@
}

## end CD changes ##

#####################################
#####################################
#####################################


#
# Functions for reviewing pull requests
#

function getLPSOrSOS() {
	TICKET=$(echo $1 | grep -i -o '\(LPS\|SOS\)\-[0-9]*')
	if [[ $TICKET == "" ]]
	then
		TICKET=$(git log -1 --pretty=%B | awk '{print $1;}')

		git branch -m "$BRANCH$TICKET"
	fi

	echo $TICKET
}


function getPullRequestNumber() {
	echo $1 | grep -i -o '\(\?\:pull-request-\)\?[0-9]*' | head -1
}

function getPullRequestTitleAndBody() {
	#
	# $1 is the LPS number
	#
	# If there is no author (IE current user is sending a new pull request) there will be no body
	#

	AUTHOR=$(gs | grep -io "'.*/" | tr -d "'/")
	BODY=$(getSubmitterGithubName)

	MESSAGE="[TECHNICAL-SUPPORT] $1

	$BODY"

	echo -n "$MESSAGE"
}

function getReviewerActualName() {
	case "$1" in
		"adolfopa") echo "Adolfo"
			;;
		"arboliveira") echo "André"
			;;
		"brandizzi") echo "Adam"
			;;
		"brianchandotcom") echo "Brian"
			;;
		"danielkocsis") echo "Daniel"
			;;
		"drewbrokke") echo "Drew"
			;;
		"dustinryerson") echo "Dustin"
			;;
		"ealonso") echo "Eudaldo"
			;;
		"epgarcia") echo "Eduardo"
			;;
		"hhuijser") echo "Hugo"
			;;
		"holatuwol") echo "Minhchau"
			;;
		"igorspasic") echo "Igor"
			;;
		"ipeychev") echo "Iliyan"
			;;
		"johnnyhowey") echo "Jon"
			;;
		"jonmak08") echo "Jon"
			;;
		"jorgeferrer") echo "Jorge"
			;;
		"juliocamarero") echo "Julio"
			;;
		"marcellustavares") echo "Marcellus"
			;;
		"matethurzo") echo "Mate"
			;;
		"mhan810") echo "Mike"
			;;
		"pei-jung") echo "Pei-Jung"
			;;
		"rotty3000") echo "Ray"
			;;
		"sergiogonzalez") echo "Sergio"
			;;
		"shinnlok") echo "Shinn"
			;;
		"shuyangzhou") echo "Shuyang"
			;;
		"topolik") echo "Tomáš"
			;;
		"zoltantakacs") echo "Zoltan"
			;;
		*) echo "Invalid choice"
			;;
	esac
}

function getReviewerJIRAName() {
	case "$1" in
		"adolfopa") echo "adolfo.perez"
			;;
		"arboliveira") echo "andre.oliveira"
			;;
		"brandizzi") echo "adam.brandizzi"
			;;
		"brianchandotcom") echo "brian.chan"
			;;
		"danielkocsis") echo "daniel.kocsis"
			;;
		"drewbrokke") echo "drew.brokke"
			;;
		"dustinryerson") echo "dustin.ryerson"
			;;
		"ealonso") echo "eudaldo.alonso"
			;;
		"epgarcia") echo "eduardo.garcia"
			;;
		"hhuijser") echo "hugo.huijser"
			;;
		"holatuwol") echo "minhchau.dang"
			;;
		"igorspasic") echo "igor.spasic"
			;;
		"ipeychev") echo "iliyan.peychev"
			;;
		"johnnyhowey") echo "jonathan.lee"
			;;
		"jonmak08") echo "jonathan.mak"
			;;
		"jorgeferrer") echo "jorge.ferrer"
			;;
		"juliocamarero") echo "julio.camarero"
			;;
		"marcellustavares") echo "marcellus.tavares"
			;;
		"matethurzo") echo "mate.thurzo"
			;;
		"mhan810") echo "michael.han"
			;;
		"pei-jung") echo "pei-jung.lan"
			;;
		"rotty3000") echo "raymond.auge"
			;;
		"jonathanmccann") echo "jonathan.mccann"
			;;
		"sergiogonzalez") echo "sergio.gonzalez"
			;;
		"shinnlok") echo "shinn.lok"
			;;
		"shuyangzhou") echo "shuyang.zhou"
			;;
		"topolik") echo "tomas.polesovsky"
			;;
		"zoltantakacs") echo "zoltan.takacs"
			;;
		*) echo "Invalid choice"
			;;
	esac
}

function getSubmitterGithubName() {
	PWD2=$(basename `pwd`)

	MASTER=""

	if [[ "$PWD2" == liferay-portal ]]
	then
		MASTER="upstream/master"
	elif [[ "$PWD2" == liferay-plugins ]]
	then
		MASTER="upstream/master"
	elif [[ "$PWD2" == liferay-portal-ee-7.0.x ]]
	then
		MASTER="upstream/ee-7.0.x"
	elif [[ "$PWD2" == liferay-portal-ee-6.2.x ]]
	then
		MASTER="upstream/ee-6.2.x"
	elif [[ "$PWD2" == liferay-portal-ee-6.1.x ]]
	then
		MASTER="upstream/ee-6.1.x"
	elif [[ "$PWD2" == liferay-plugins-ee-7.0.x ]]
	then
		MASTER="upstream/ee-7.0.x"
	elif [[ "$PWD2" == liferay-plugins-ee-6.2.x ]]
	then
		MASTER="upstream/ee-6.2.x"
	elif [[ "$PWD2" == liferay-plugins-ee-6.1.x ]]
	then
		MASTER="upstream/ee-6.1.x"
	else
		exit
	fi

	SUBMITTERS=( $(git log HEAD...$MASTER --pretty=format:"%ae") )

	SUBMITTERS=( $(echo "${SUBMITTERS[@]}" | tr ' ' '\n' | sort -u | tr '\n' ' ') )

	if [ ${#SUBMITTERS[@]} -eq 0 ]; then
		echo ""
	else
		BODY="/cc"
		for SUBMITTER in "${SUBMITTERS[@]}"
		do
			case "$SUBMITTER" in
				"Alec.Shay@liferay.com") SUBMITTER=" @Alec-Shay"
					;;
				"bryan.engler@liferay.com") SUBMITTER=" @BryanEngler"
					;;
				"christopher.kian@liferay.com") SUBMITTER=" @ChrisKian"
					;;
				"dustin.ryerson@liferay.com") SUBMITTER=" @dustinryerson"
					;;
				"eric.yan@liferay.com") SUBMITTER=" @ericyanLr"
					;;
				"gregory.bretall@liferay.com") SUBMITTER=" @gregory-bretall"
					;;
				"joshua.cords@liferay.com") SUBMITTER=" @joshuacords"
					;;
				"michael.bowerman@liferay.com") SUBMITTER=" @mbowerman"
					;;
				"michael.prigge@liferay.com") SUBMITTER=" @MichaelPrigge"
					;;
				"nolan.chan@liferay.com") SUBMITTER=" @NolanChan"
					;;
				"Jonathan.McCann@liferay.com") SUBMITTER=" @JonathanMcCann"
					;;
				"spencer.woo@liferay.com") SUBMITTER=" @SpencerWoo"
					;;
				*) SUBMITTER=""
			esac

			BODY="$BODY$SUBMITTER"
		done

		if [ "$BODY" != "/cc" ]; then
			echo $BODY
		fi
	fi
}

function gitprclose() {
	# $1 is the repository of the person being reviewed (liferay-portal, liferay-portal-ee, etc.)
	# $2 is the pull request number
	curl --silent --request PATCH -u $OAUTHTOKEN:x-oauth-basic --data '{"state":"closed"}' https://api.github.com/repos/samziemer/$1/pulls/$2 > /dev/null
}

function gitprsubmitcomment() {
	#
	# $1 is the next reviewer
	# $2 is the link to the next pull request
	# $3 is the repository of the person being reviewed
	# $4 is the pull request number
	#
	# Models: https://api.github.com/repos/samziemer/liferay-portal/issues/347/comments
	REVIEWERACTUALNAME=$(getReviewerActualName $1)

	BODY="Reviewed and sent to $REVIEWERACTUALNAME for further review: $2"

	curl --silent -u $OAUTHTOKEN:x-oauth-basic --data '{"body":"'"$BODY"'"}' https://api.github.com/repos/samziemer/$3/issues/$4/comments > /dev/null
}

function gitprf() {
	# Get the current directory
	PWD2=$(basename `pwd`)

	# Get pull request data from GitHub based on the current directory
	if [[ "$PWD2" == liferay-portal ]]
	then
		git checkout https://github.com/samziemer/liferay-portal/pull/$1
	elif [[ "$PWD2" == liferay-plugins ]]
	then
		git checkout https://github.com/samziemer/liferay-plugins/pull/$1
	elif [[ "$PWD2" == liferay-portal-ee-* ]]
	then
		git checkout https://github.com/samziemer/liferay-portal-ee/pull/$1
	elif [[ "$PWD2" == liferay-plugins-ee-* ]]
	then
		git checkout https://github.com/samziemer/liferay-plugins-ee/pull/$1
	fi

	# Get the current branch
	BRANCH=$(git rev-parse --abbrev-ref HEAD)

	# Get the LPS or SOS to populate the pull request title
	LPS=$(getLPSOrSOS $BRANCH)

	# Open the corresponding pull request and JIRA LPS/SOS in the browser
	#google-chrome https://issues.liferay.com/browse/$LPS
	# google-chrome https://github.com/samziemer/liferay-portal/pull/$1

	# JIRA search to open LPP
	# project = LPP and issue in LinkedIssues($LPS)
	#LPP=$(curl -s -n -X GET -H "Content-Type: application/json" "https://issues.liferay.com/rest/api/2/search?jql=project+%3D+LPP+AND+issue+in+linkedIssues('$LPS'%2C+'relates'%2C+'is+related+to')&maxResults=1&fields=id,key" | jq -r '.issues | .[0].key')

	#if [[ $LPP != "null" ]]
	#then
	#	echo "lpp"
	#	google-chrome https://issues.liferay.com/browse/$LPP
	#fi
}

function gitprs() {
	# Get the current directory
	PWD2=$(basename `pwd`)

	# Get the current branch
	BRANCH=$(git rev-parse --abbrev-ref HEAD)

	# Get the LPS or SOS to populate the pull request title
	LPS=$(getLPSOrSOS $BRANCH)

	# Get the pull request number
	PULLREQUESTNUMBER=$(getPullRequestNumber $BRANCH)

	if [[ "$PWD2" == liferay-portal ]]
	then
		REMOTEBRANCH="master"
		REPO="liferay-portal"
	elif [[ "$PWD2" == liferay-plugins ]]
	then
		REMOTEBRANCH="master"
		REPO="liferay-plugins"
	elif [[ "$PWD2" == liferay-portal-ee-7.0.x ]]
	then
		REMOTEBRANCH="ee-7.0.x"
		REPO="liferay-portal-ee"
	elif [[ "$PWD2" == liferay-portal-ee-6.2.x ]]
	then
		REMOTEBRANCH="ee-6.2.x"
		REPO="liferay-portal-ee"
	elif [[ "$PWD2" == liferay-portal-ee-6.1.x ]]
	then
		REMOTEBRANCH="ee-6.1.x"
		REPO="liferay-portal-ee"
	elif [[ "$PWD2" == liferay-portal-ee-6.0.x ]]
	then
		REMOTEBRANCH="ee-6.0.x"
		REPO="liferay-portal-ee"
	elif [[ "$PWD2" == liferay-plugins-ee-7.0.x ]]
	then
		REMOTEBRANCH="ee-7.0.x"
		REPO="liferay-plugins-ee"
	elif [[ "$PWD2" == liferay-plugins-ee-6.2.x ]]
	then
		REMOTEBRANCH="ee-6.2.x"
		REPO="liferay-plugins-ee"
	elif [[ "$PWD2" == liferay-plugins-ee-6.1.x ]]
	then
		REMOTEBRANCH="ee-6.1.x"
		REPO="liferay-plugins-ee"
	elif [[ "$PWD2" == liferay-plugins-ee-6.0.x ]]
	then
		REMOTEBRANCH="ee-6.0.x"
		REPO="liferay-plugins-ee"
	fi

	echo -en "\nDoes this fix need a test? "
	read TEST

	if [[ "$TEST" == y ]]
	then
		return 0
	fi

	echo "GitHub Usernames: $GITHUB_USERNAMES"
	echo -n "Enter the reviewer and press [ENTER]: "
	read REVIEWER

	URL=$(git pull-request -m "$(getPullRequestTitleAndBody $LPS)" -b $REVIEWER:$REMOTEBRANCH -h samziemer:$BRANCH)

	echo -n $URL | xclip -selection clipboard

	# Write a comment on the reviewed pull request
	gitprsubmitcomment $REVIEWER $URL $REPO $PULLREQUESTNUMBER
	# Close the reviewed pull request
	gitprclose $REPO $PULLREQUESTNUMBER

	# Push the LPS through the proper workflow
	pushLPSThroughWorkflow $REVIEWER $LPS $URL
}

function gitprsb() {
	# Get the current directory
	PWD2=$(basename `pwd`)

	# Get the current branch
	BRANCH=$(git rev-parse --abbrev-ref HEAD)

	# Get the LPS or SOS to populate the pull request title
	LPS=$(getLPSOrSOS $BRANCH)

	# Get the pull request number
	PULLREQUESTNUMBER=$(getPullRequestNumber $BRANCH)

	if [[ "$PWD2" == liferay-portal ]]
	then
		REMOTEBRANCH="master"
		REPO="liferay-portal"
	elif [[ "$PWD2" == liferay-plugins ]]
	then
		REMOTEBRANCH="master"
		REPO="liferay-plugins"
	elif [[ "$PWD2" == liferay-portal-ee-6.2.x ]]
	then
		REMOTEBRANCH="ee-6.2.x"
		REPO="liferay-portal-ee"
	elif [[ "$PWD2" == liferay-portal-ee-6.1.x ]]
	then
		REMOTEBRANCH="ee-6.1.x"
		REPO="liferay-portal-ee"
	elif [[ "$PWD2" == liferay-portal-ee-6.0.x ]]
	then
		REMOTEBRANCH="ee-6.0.x"
		REPO="liferay-portal-ee"
	fi

	git pull-request -m $LPS -b dustinryerson:$REMOTEBRANCH -h samziemer:$BRANCH
}

function pushLPSThroughWorkflow() {
	#
	# $1 is the next reviewer
	# $2 is the LPS/SOS identifier
	# $3 is the next pull request
	#
	REVIEWERJIRANAME=$(getReviewerJIRAName $1)

	STATUS=$(curl -s -n -X GET -H "Content-Type: application/json" https://issues.liferay.com/rest/api/2/issue/$2?fields=status | jq -r '.fields.status.name')

	if [[ "$STATUS" == "In Review" ]]
	then
		curl -n -X POST --data '{"fields": {"assignee":{"name":"'$REVIEWERJIRANAME'"}, "customfield_10421":"'$3'"}, "transition": {"id":"981"}}' -H "Content-Type: application/json" https://issues.liferay.com/rest/api/2/issue/$2/transitions
	else
		curl -n -X POST --data '{"fields": {"assignee":{"name":"'$REVIEWERJIRANAME'"}, "customfield_10421":"'$3'"}, "transition": {"id":"731"}}' -H "Content-Type: application/json" https://issues.liferay.com/rest/api/2/issue/$2/transitions
	fi
}

function pr() {
	BRANCH=$(git rev-parse --abbrev-ref HEAD)

	PWD2=$(basename `pwd`)

	case $PWD2 in
		liferay-portal-ee-6.0.x)
			echo -e "\n""\033[32mPull Request for 6.0.x\033[0m"
			google-chrome https://github.com/samziemer/liferay-portal-ee/pull/new/hhuijser:ee-6.0.x...samziemer:$BRANCH
			;;
		liferay-portal-ee-6.1.x)
			echo -e "\n""\033[32mPull Request for 6.1.x\033[0m"
			google-chrome https://github.com/samziemer/liferay-portal-ee/pull/new/hhuijser:ee-6.1.x...samziemer:$BRANCH
			;;
		liferay-portal-ee-6.2.x)
			echo -e "\n""\033[32mPull Request for 6.2.x\033[0m"
			google-chrome https://github.com/samziemer/liferay-portal-ee/pull/new/hhuijser:ee-6.2.x...samziemer:$BRANCH
			;;	
		liferay-portal)
			echo -e "\n""\033[32mPull Request for Master\033[0m"
			google-chrome https://github.com/samziemer/liferay-portal/pull/new/hhuijser:master...samziemer:$BRANCH
			;;
		liferay-plugins-ee)
			echo -e "\n""\033[32mPull Request for Plugins $1\033[0m"
			google-chrome https://github.com/samziemer/liferay-plugins-ee/pull/new/hhuijser:ee-$1...samziemer:$BRANCH
			;;
		liferay-plugins)
			echo -e "\n""\033[32mPull Request for Plugins Master\033[0m"
			google-chrome https://github.com/samziemer/liferay-plugins/pull/new/hhuijser:master...samziemer:$BRANCH
			;;
		*)
	esac
}


#### Pull requests ###
#
#function gitprf() {
#	# Get the current directory
#	BNAME=${PWD2##/*/}
#
#	# Get pull request data from GitHub based on the current directory
#	if [[ "$BNAME" == liferay-portal ]]
#	then
#		git checkout -f https://github.com/SamZiemer/liferay-portal/pull/$1
#	elif [[ "$BNAME" == liferay-plugins ]]
#	then
#		git checkout -f https://github.com/SamZiemer/liferay-plugins/pull/$1
#	elif [[ "$BNAME" == liferay-portal-ee* ]]
#	then
#		git checkout -f https://github.com/SamZiemer/liferay-portal-ee/pull/$1
#	elif [[ "$BNAME" == liferay-plugins-ee* ]]
#	then
#		git checkout -f https://github.com/SamZiemer/liferay-plugins-ee/pull/$1
#	elif [[ "$BNAME" == master-private ]]
#	then
#		git checkout -f https://github.com/SamZiemer/liferay-portal-ee/pull/$1
#	fi
#
#	# Get the current branch
#	#BRANCH=$(git rev-parse --abbrev-ref HEAD)
#
#	# Get the LPS or SOS to populate the pull request title
#	#LPS=$(getLPSOrSOS $BRANCH)
#
#	# Open the corresponding pull request and JIRA LPS/SOS in the browser
#	#/usr/bin/open -a "/Applications/Google Chrome.app" "https://issues.liferay.com/browse/$LPS"
#	#if [[ "$BNAME" == liferay-portal-ee* ]]
#	#then
#	#	/usr/bin/open -a "/Applications/Google Chrome.app" "https://github.com/SamZiemer/liferay-portal-ee/pull/$1"
#	#elif [[ "$BNAME" == liferay-portal ]]
#	#then
#	#	/usr/bin/open -a "/Applications/Google Chrome.app" "https://github.com/SamZiemer/liferay-portal/pull/$1"
#	#fi
#
#	# JIRA search to open LPP
#	# project = LPP and issue in LinkedIssues($LPS)
#}
#
#function getPullRequestNumber() {
#	echo $1 | grep -i -o '\(\?\:pull-request-\)\?[0-9]*' | head -1
#}
#
#function getPullRequestTitleAndBody() {
#	#
#	# $1 is the LPS number
#	#
#	# If there is no author (IE current user is sending a new pull request) there will be no body
#	#
#
#	AUTHOR=$(gs | grep -io "'.*/" | tr -d "'/")
#	BODY=""
#
#	if [[ ! -z "$AUTHOR" ]]
#	then
#		BODY="/cc @$AUTHOR"
#	fi
#
#	MESSAGE="[TECHNICAL-SUPPORT] $1
#
#	$BODY"
#
#	echo -n "$MESSAGE"
#}
#
#function gitprs() {
#	# Get the current directory
#	PWD22=$(basename `pwd`)
#
#	# Get the current branch
#	BRANCH=$(git rev-parse --abbrev-ref HEAD)
#
#	# Get the LPS or SOS to populate the pull request title
#	LPS=$(getLPSOrSOS $BRANCH)
#
#	# Get the pull request number
#	PULLREQUESTNUMBER=$(getPullRequestNumber $BRANCH)
#
#	if [[ "$PWD22" == liferay-portal ]]
#	then
#		REVIEWER="jonathanmccann"
#		REMOTEBRANCH="master"
#		REPO="liferay-portal"
#	elif [[ "$PWD22" == liferay-portal-ee-6.2.x ]]
#	then
#		REVIEWER="michaelprigge"
#		REMOTEBRANCH="ee-6.2.x"
#		REPO="liferay-portal-ee"
#	elif [[ "$PWD22" == liferay-portal-ee-7.0.x ]]
#	then
#		REVIEWER="michaelprigge"
#		REMOTEBRANCH="7.0.x"
#		REPO="liferay-portal-ee"
#	elif [[ "$PWD22" == liferay-portal-ee-6.1.x ]]
#	then
#		REVIEWER="michaelprigge"
#		REMOTEBRANCH="ee-6.1.x"
#		REPO="liferay-portal-ee"
#	elif [[ "$PWD22" == liferay-portal-ee ]]
#	then
#		REVIEWER="michaelprigge"
#		REMOTEBRANCH="7.0.x"
#		REPO="liferay-portal-ee"
#	elif [[ "$PWD22" == master-private ]]
#	then
#		REVIEWER="jonathanmccann"
#		REMOTEBRANCH="master-private"
#		REPO="liferay-portal-ee"
#	elif [[ "$PWD2" == liferay-plugins-ee-6.2.x ]]
#	then
#		REVIEWER="michaelprigge"
#		REMOTEBRANCH="ee-6.2.x"
#		REPO="liferay-plugins-ee"
#	elif [[ "$PWD2" == liferay-plugins-ee-6.1.x ]]
#	then
#		REVIEWER="michaelprigge"
#		REMOTEBRANCH="ee-6.1.x"
#		REPO="liferay-plugins-ee"
#	fi
#
#	URL=$(git pull-request -m "$(getPullRequestTitleAndBody $LPS)" -b $REVIEWER:$REMOTEBRANCH -h SamZiemer:$BRANCH)
#
#	echo $URL
#	echo -n $URL | xclip -selection clipboard
#
#	# Write a comment on the reviewed pull request
#	gitprsubmitcomment $REVIEWER $URL $REPO $PULLREQUESTNUMBER
#
#	# Close the reviewed pull request
#	gitprclose $REPO $PULLREQUESTNUMBER
#}
#
#function gitprsubmitcomment() {
#	#
#	# $1 is the next reviewer
#	# $2 is the link to the next pull request
#	# $3 is the repository of the person being reviewed
#	# $4 is the pull request number
#	#
#	# Models: https://api.github.com/repos/samziemer/liferay-portal/issues/347/comments
#	REVIEWERACTUALNAME="Jonathan McCann"
#
#	BODY="Reviewed and sent to $REVIEWERACTUALNAME for further review: $2"
#
#	curl -u $OAUTHTOKEN:x-oauth-basic --data '{"body":"'"$BODY"'"}' https://api.github.com/repos/SamZiemer/$3/issues/$4/comments > /dev/null
#}
#
#function gitprclose() {
#	# $1 is the repository of the person being reviewed (liferay-portal, liferay-portal-ee, etc.)
#	# $2 is the pull request number
#	curl --request PATCH -u $OAUTHTOKEN:x-oauth-basic --data '{"state":"closed"}' https://api.github.com/repos/samziemer/$1/pulls/$2 > /dev/null
#}
#
#
#function getLPSOrSOS() {
#	echo $1 | grep -i -o '\(LPS\|SOS\)\-[0-9]*'
#}
#
#### end Pull requests ###


############# FUNCTIONS END ##################

