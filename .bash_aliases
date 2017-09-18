ANT_OPTS="-Xmx4096m -XX:MaxPermSize=1024m"
MYSQL_HOME="/usr/local/mysql/bin"
PATH=$PATH:~/scripts:~/Library/PackageManager/bin:$MYSQL_HOME:/Users/ziemers/miniconda3/bin
export ANT_OPTS PATH

## ALIASES ##
##alias aa='ant all'
alias ad='ant deploy'
alias adf='ant deploy-fast'
alias afs='ant format-source-current-branch'
alias clean='git checkout .; git clean -fd;'
alias clear="clear && printf '\e[3J'"
alias dbg="clear && printf '\e[3J'; ./catalina.sh jpda run;"
alias dsr='find . -name '.DS_Store' -type f -delete'
alias git="hub"
alias gbv="git bisect visualize"
alias gc="git checkout"
alias gcp="git cherry-pick"
alias gcpc="git cherry-pick --continue"
alias gfo='git fetch origin'
alias gfu='git fetch upstream'
alias gitclean='git clean -xfde "*.iml" -e ".idea/"'
alias gmt="git mergetool"
alias gl='git stash list'
alias gs='git status'
alias ignored='git ls-files -v | grep ^[a-z]'
alias java7="source java7.sh"
alias java8="source java8.sh"
alias jca="java -jar ~/Applications/jca/jca.jar"
alias kazissh="ssh sam@jktest.hopto.org"
alias mysqlp='mysql -u root -ppassword'
alias run="clear && printf '\e[3J' ./catalina.sh run;"
alias s61="cd /Users/ziemers/servers/liferay-portal-ee-6.1.x/tomcat-7.0.42/bin/"
alias s62="cd /Users/ziemers/servers/liferay-portal-ee-6.2.x/tomcat-7.0.42/bin/"
alias see="cd /Users/ziemers/servers/liferay-portal-ee/tomcat-8.0.32/bin/"
alias smaster="cd /Users/ziemers/servers/liferay-portal/tomcat-8.0.32/bin/"
alias sp3="git checkout fix-pack-base-6130-sp3"
alias sp7="git checkout fix-pack-base-6210-sp7"
alias sp10="git checkout fix-pack-base-6210-sp10"
alias sp13="git checkout fix-pack-base-6210-sp13"
alias sp15="git checkout fix-pack-base-6210-sp15"
alias sp20="git checkout fix-pack-base-6210-sp20"
alias r61="cd /Users/ziemers/repos/liferay-portal-ee-6.1.x"
alias r62="cd /Users/ziemers/repos/liferay-portal-ee-6.2.x"
alias rmaster="cd /Users/ziemers/repos/liferay-portal"
alias ree="cd /Users/ziemers/repos/liferay-portal-ee"
alias refreshbash="source ~/.profile"
alias bsp7="cd ~/bundles/liferay-portal-6.2.10-sp7/tomcat-7.0.42/bin/"
alias b62="cd ~/bundles/liferay-portal-6.2.10/tomcat-7.0.42/bin/"
alias b6120="cd ~/bundles/liferay-portal-6.1.20/tomcat-7.0.27/bin/"
alias taketd="jstackSeries.sh"
alias upgrade='clear && printf "\e[3J"; java -jar com.liferay.portal.tools.db.upgrade.client.jar -j="-Xmx4096m"'
alias upgradedbg='clear && printf "\e[3J"; java -jar com.liferay.portal.tools.db.upgrade.client.jar -j="-Xmx4096m -Dfile.encoding=UTF8 -Duser.country=US -Duser.language=en -Duser.timezone=GMT -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000"'

alias ss='show_stash'
alias as='apply_stash'
alias gmu='merge_upstream'

############# FUNCTIONS BEGIN ################

MCD_RD_CLONE_PATH=/Users/ziemers/repos/liferay-faster-deploy

function subrepobp() {
        SUBREPO_ROOT=/Users/ziemers/repos \
                ${MCD_RD_CLONE_PATH}/patcher/subrepobp $@
}

function aa() {
	RNAME=${PWD##/*/}

	if [[ "$RNAME" == liferay-portal ]]
	then
		#${MCD_RD_CLONE_PATH}/nodejs/safeant all
		ant all
	elif [[ "$RNAME" == liferay-portal-ee ]]
	then
		#${MCD_RD_CLONE_PATH}/nodejs/safeant all
		ant all
	elif [[ "$RNAME" == liferay-portal-ee-6.1.x ]]
	then
		ant all
	elif [[ "$RNAME" == liferay-portal-ee-6.2.x ]]
	then
		ant all
	elif [[ "$PWD" == *liferay-plugins-ee* ]]
	then
		ant all
	elif [[ "$PWD" == *-private* ]]
	then
		ant all
	else
		echo "Not a build repo"
	fi
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

sublime() {
	/Applications/Sublime\ Text\ 2.app/Contents/SharedSupport/bin/subl $1
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

## CD changes ##

cd() {
        . /Users/ziemers/repos/liferay-faster-deploy/gitcd/gitcd $@
}

cdb() {
        . /Users/ziemers/repos/liferay-faster-deploy/gitcd/gitcdb $@
}

cdp() {
        . /Users/ziemers/repos/liferay-faster-deploy/gitcd/gitcdp $@
}

## end CD changes ##

#####################################
#####################################
#####################################


### Pull requests ###

function gitprf() {
	# Get the current directory
	BNAME=${PWD##/*/}

	# Get pull request data from GitHub based on the current directory
	if [[ "$BNAME" == liferay-portal ]]
	then
		git checkout -f https://github.com/SamZiemer/liferay-portal/pull/$1
	elif [[ "$BNAME" == liferay-plugins ]]
	then
		git checkout -f https://github.com/SamZiemer/liferay-plugins/pull/$1
	elif [[ "$BNAME" == liferay-portal-ee* ]]
	then
		git checkout -f https://github.com/SamZiemer/liferay-portal-ee/pull/$1
	elif [[ "$BNAME" == liferay-plugins-ee* ]]
	then
		git checkout -f https://github.com/SamZiemer/liferay-plugins-ee/pull/$1
	elif [[ "$BNAME" == master-private ]]
	then
		git checkout -f https://github.com/SamZiemer/liferay-portal-ee/pull/$1
	fi

	# Get the current branch
	#BRANCH=$(git rev-parse --abbrev-ref HEAD)

	# Get the LPS or SOS to populate the pull request title
	#LPS=$(getLPSOrSOS $BRANCH)

	# Open the corresponding pull request and JIRA LPS/SOS in the browser
	#/usr/bin/open -a "/Applications/Google Chrome.app" "https://issues.liferay.com/browse/$LPS"
	#if [[ "$BNAME" == liferay-portal-ee* ]]
	#then
	#	/usr/bin/open -a "/Applications/Google Chrome.app" "https://github.com/SamZiemer/liferay-portal-ee/pull/$1"
	#elif [[ "$BNAME" == liferay-portal ]]
	#then
	#	/usr/bin/open -a "/Applications/Google Chrome.app" "https://github.com/SamZiemer/liferay-portal/pull/$1"
	#fi

	# JIRA search to open LPP
	# project = LPP and issue in LinkedIssues($LPS)
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
	BODY=""

	if [[ ! -z "$AUTHOR" ]]
	then
		BODY="/cc @$AUTHOR"
	fi

	MESSAGE="[TECHNICAL-SUPPORT] $1

	$BODY"

	echo -n "$MESSAGE"
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
		REVIEWER="jonathanmccann"
		REMOTEBRANCH="master"
		REPO="liferay-portal"
	elif [[ "$PWD2" == liferay-portal-ee-6.2.x ]]
	then
		REVIEWER="michaelprigge"
		REMOTEBRANCH="ee-6.2.x"
		REPO="liferay-portal-ee"
	elif [[ "$PWD2" == liferay-portal-ee-6.1.x ]]
	then
		REVIEWER="michaelprigge"
		REMOTEBRANCH="ee-6.1.x"
		REPO="liferay-portal-ee"
	elif [[ "$PWD2" == liferay-portal-ee ]]
	then
		REVIEWER="michaelprigge"
		REMOTEBRANCH="7.0.x"
		REPO="liferay-portal-ee"
	elif [[ "$PWD2" == master-private ]]
	then
		REVIEWER="jonathanmccann"
		REMOTEBRANCH="master-private"
		REPO="liferay-portal-ee"
	elif [[ "$PWD" == liferay-plugins-ee-6.2.x ]]
	then
		REVIEWER="michaelprigge"
		REMOTEBRANCH="ee-6.2.x"
		REPO="liferay-plugins-ee"
	elif [[ "$PWD" == liferay-plugins-ee-6.1.x ]]
	then
		REVIEWER="michaelprigge"
		REMOTEBRANCH="ee-6.1.x"
		REPO="liferay-plugins-ee"
	fi

	URL=$(git pull-request -m "$(getPullRequestTitleAndBody $LPS)" -b $REVIEWER:$REMOTEBRANCH -h SamZiemer:$BRANCH)

	echo $URL
	echo -n $URL | pbcopy

	# Write a comment on the reviewed pull request
	gitprsubmitcomment $REVIEWER $URL $REPO $PULLREQUESTNUMBER

	# Close the reviewed pull request
	gitprclose $REPO $PULLREQUESTNUMBER
}

function gitprsubmitcomment() {
	#
	# $1 is the next reviewer
	# $2 is the link to the next pull request
	# $3 is the repository of the person being reviewed
	# $4 is the pull request number
	#
	# Models: https://api.github.com/repos/jonathanmccann/liferay-portal/issues/347/comments
	REVIEWERACTUALNAME="Jonathan McCann"

	BODY="Reviewed and sent to $REVIEWERACTUALNAME for further review: $2"

	curl -u $OAUTHTOKEN:x-oauth-basic --data '{"body":"'"$BODY"'"}' https://api.github.com/repos/SamZiemer/$3/issues/$4/comments > /dev/null
}

function gitprclose() {
	# $1 is the repository of the person being reviewed (liferay-portal, liferay-portal-ee, etc.)
	# $2 is the pull request number
	curl --request PATCH -u $OAUTHTOKEN:x-oauth-basic --data '{"state":"closed"}' https://api.github.com/repos/jonathanmccann/$1/pulls/$2 > /dev/null
}


function getLPSOrSOS() {
	echo $1 | grep -i -o '\(LPS\|SOS\)\-[0-9]*'
}

### end Pull requests ###


############# FUNCTIONS END ##################

