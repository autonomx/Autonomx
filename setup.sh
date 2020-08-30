#!/usr/bin/env sh
export LC_ALL=en_US.UTF-8

cd "$(dirname ${BASH_SOURCE[0]})"
cd automation

# remove maven directory
rm -r ./.maven

mkdir ./.maven

# download maven jar
curl -SL https://github.com/autonomx/mavenRunner/releases/latest/download/maven.jar > ./.maven/maven.jar

java -jar ./.maven/maven.jar clean install -U -DskipTests 