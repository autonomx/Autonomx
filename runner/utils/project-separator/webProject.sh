#!/bin/bash -l
export LC_ALL=en_US.UTF-8

VERSION=$1

cd "$(dirname ${BASH_SOURCE[0]})"
# return to outside of autnomx directory
cd ../../../../
# remove web project 
rm -rf ./autonomx-web

# create sub projects
echo copy web project
rsync -r ./autonomx/ ./autonomx-web/

#### setup web
cd ./autonomx-web/

# remove temp folders
rm -rf ./automation/target
rm -rf ./automation/test-output

# remove git folders
rm -rf ./.git

# remove project separator
rm -rf ./runner/utils/project-separator

# remove apiTestData
echo remove apiTestData 
 rm -rf apiTestData

 # remove maven and script server folder
rm -rf ./runner/utils/maven
rm -rf ./runner/utils/script-server

 # remove bitbucket pipelines
rm -rf ./bitbucket-pipelines.yml
rm -rf ./azure-pipelines.yml

# remove modules 
echo remove modules 
rm -rf automation/src/main/java/module/androidApp
rm -rf automation/src/main/java/module/iosApp
rm -rf automation/src/main/java/module/services
rm -rf automation/src/main/java/module/serviceUiIntegration
rm -rf automation/src/main/java/module/windowsApp
rm -rf automation/src/main/java/module/framework
# remove tests
echo remove tests
rm -rf automation/src/test/java/test/module/android
rm -rf automation/src/test/java/test/module/ios
rm -rf automation/src/test/java/test/module/service
rm -rf automation/src/test/java/test/module/serviceIntegration
rm -rf automation/src/test/java/test/module/win
rm -rf automation/src/test/java/test/module/framework
# remove suites
echo remove suites
rm -rf automation/suites/androidSmokeTests.xml
rm -rf automation/suites/completeTestSuite.xml
rm -rf automation/suites/iosSmokeTests.xml
rm -rf automation/suites/servicesTests.xml
rm -rf automation/suites/serviceIntegration.xml
rm -rf automation/suites/winSmokeTests.xml
rm -rf automation/suites/frameworkFunctionalTests.xml
rm -rf automation/suites/frameworkUiTests.xml
rm -rf automation/suites/frameworkMessageQueueTests.xml.xml

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
zip -r ./autonomx-web-$VERSION.zip ./autonomx-web

# remove non zip project
rm -rf ./autonomx-web












