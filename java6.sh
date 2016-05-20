#!/bin/bash
#Java 6
sudo update-alternatives --install "/usr/bin/java" "java" "/opt/java/jdk1.6.0_45/bin/java" 1
sudo update-alternatives --set java /opt/java/jdk1.6.0_45/bin/java
java -version

sudo update-alternatives --install "/usr/bin/javac" javac "/opt/java/jdk1.6.0_45/bin/javac" 1
sudo update-alternatives --config javac
javac -version