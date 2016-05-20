#!/bin/bash
#Java 7
sudo update-alternatives --install "/usr/bin/java" "java" "/opt/java/jdk1.7.0_71/bin/java" 1
sudo update-alternatives --set java /opt/java/jdk1.7.0_71/bin/java
java -version

sudo update-alternatives --install "/usr/bin/javac" javac "/opt/java/jdk1.7.0_71/bin/javac" 1
sudo update-alternatives --config javac
javac -version