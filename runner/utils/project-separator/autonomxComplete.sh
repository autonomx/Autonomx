#!/bin/bash -l
export LC_ALL=en_US.UTF-8

VERSION=$1

cd "$(dirname ${BASH_SOURCE[0]})"
# return to outside of autnomx directory
cd ../../../../

# remove web project 
rm -rf ./autonomx-complete

# create sub projects
echo copy complete project
rsync -r ./autonomx/ ./autonomx-complete/

#### setup web
cd ./autonomx-complete/

# remove temp folders
rm -rf ./automation/target
rm -rf ./automation/test-output

# remove git folders
rm -rf ./.git

# remove bitbucket pipelines
rm -rf ./bitbucket-pipelines.yml
rm -rf ./azure-pipelines.yml

# remove project separator
rm -rf ./runner/utils/project-separator

# remove maven and script server folder
rm -rf ./runner/utils/maven
rm -rf ./runner/utils/script-server

# remove maven central
rm -rf ./automation/pom.xml
mv ./automation/maven-central/pom.xml ./automation/pom.xml
rm -rf ./automation/maven-central

# change version to latest autnonomx-core
cd ./automation/
mvn versions:use-latest-versions -Dincludes=io.autonomx:autonomx-core
rm -rf ./pom.xml.versionsBackup
# generate generated code
mvn clean compile
cd ../

# remove runner 
echo set runner 
bash runner/generateScripts.sh

# add to zip
cd ../
zip -r autonomx-complete-$VERSION.zip ./autonomx-complete

# remove non zip project
rm -rf ./autonomx-complete












