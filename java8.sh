#!/bin/bash
#Java 8
	sudo /usr/bin/update-alternatives --install "/usr/bin/java" "java" "/opt/java/jdk1.8.0_144/bin/java" 1
	sudo /usr/bin/update-alternatives --set java /opt/java/jdk1.8.0_144/bin/java
	sudo /usr/bin/update-alternatives --install "/usr/bin/javac" "javac" "/opt/java/jdk1.8.0_144/bin/javac" 1
	sudo /usr/bin/update-alternatives --set javac /opt/java/jdk1.8.0_144/bin/javac
	export JAVA_HOME=/opt/java/jdk1.8.0_144
	export JRE_HOME=/opt/java/jdk1.8.0_144/jre