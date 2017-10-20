#!/bin/bash
#Java 7
	sudo update-alternatives --install "/usr/bin/java" "java" "/opt/java/jdk1.7.0_80/bin/java" 1
	sudo update-alternatives --set java /opt/java/jdk1.7.0_80/bin/java
	sudo update-alternatives --install "/usr/bin/javac" "javac" "/opt/java/jdk1.7.0_80/bin/javac" 1
	sudo update-alternatives --set javac /opt/java/jdk1.7.0_80/bin/javac
	export JAVA_HOME=/opt/java/jdk1.7.0_80
	export JRE_HOME=/opt/java/jdk1.7.0_80/jre