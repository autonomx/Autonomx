#!/bin/bash -l
export LC_ALL=en_US.UTF-8

VERSION=$1

cd "$(dirname ${BASH_SOURCE[0]})"
# return to outside of autnomx directory
cd ../../../../
# remove webWithService project 
rm -rf ./autonomx-webWithService

# create sub projects
echo copy webWithService project
rsync -r ./autonomx/ ./autonomx-webWithService/

#### setup web
cd ./autonomx-webWithService/

# remove temp folders
rm -rf ./automation/target
rm -rf ./automation/test-output

# remove git folders
rm -rf ./.git

# remove bitbucket pipelines
rm -rf ./bitbucket-pipelines.yml

# remove modules 
echo remove modules 
rm -rf automation/src/main/java/module/androidApp
rm -rf automation/src/main/java/module/iosApp
rm -rf automation/src/main/java/module/windowsApp
# remove tests
echo remove tests
rm -rf automation/src/test/java/test/module/android
rm -rf automation/src/test/java/test/module/ios
rm -rf automation/src/test/java/test/module/win
# remove suites
echo remove suites
rm -rf automation/suites/androidSmokeTests.xml
rm -rf automation/suites/completeTestSuite.xml
rm -rf automation/suites/iosSmokeTests.xml
rm -rf automation/suites/winSmokeTests.xml
# remove resources
echo remove resources
rm -rf automation/resources/eurika.app
rm -rf automation/resources/selendroid.apk

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
echo remove runner 
bash runner/generateScripts.sh

# add to zip
cd ../
zip -r autonomx-webWithService-$VERSION.zip ./autonomx-webWithService

# remove non zip project
rm -rf ./autonomx-webWithService












