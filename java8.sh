#!/bin/bash
#Java 8
sudo update-alternatives --install "/usr/bin/java" "java" "/opt/java/jdk1.8.0_51/bin/java" 1
sudo update-alternatives --set java /opt/java/jdk1.8.0_51/bin/java
JRE_HOME=/opt/java/jdk1.8.0_51/jre
JAVA_HOME=/opt/java/jdk1.8.0_51
java -version

sudo update-alternatives --install "/usr/bin/javac" javac "/opt/java/jdk1.8.0_51/bin/javac" 1
sudo update-alternatives --config javac
javac -version