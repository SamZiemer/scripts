#!/bin/bash
#Java 7 openjdk
sudo update-alternatives --install "/usr/bin/java" "java" "/usr/lib/jvm/java-7-openjdk-amd64/bin/java" 1
sudo update-alternatives --set java /usr/lib/jvm/java-7-openjdk-amd64/bin/java
java -version

sudo update-alternatives --install "/usr/bin/javac" javac "/usr/lib/jvm/java-7-openjdk-amd64/bin/javac" 1
sudo update-alternatives --config javac
javac -version