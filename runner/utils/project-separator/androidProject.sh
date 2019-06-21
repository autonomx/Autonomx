#!/bin/bash -l
export LC_ALL=en_US.UTF-8

VERSION=$1

cd "$(dirname ${BASH_SOURCE[0]})"
# return to outside of autnomx directory
cd ../../../../
# remove web project 
rm -rf ./autonomx-android

# create sub projects
echo copy android project
rsync -r ./autonomx/ ./autonomx-android/


#### setup android
cd ./autonomx-android/

# remove temp folders
rm -rf ./automation/target/classes
rm -rf ./automation/target/maven-status
rm -rf ./automation/target/test-classes
rm -rf ./automation/test-output

# remove git folders
rm -rf ./.git

# remove apiTestData
echo remove apiTestData 
 rm -rf apiTestData

# remove modules 
echo remove modules 
rm -rf automation/src/main/java/module/webApp
rm -rf automation/src/main/java/module/iosApp
rm -rf automation/src/main/java/module/services
rm -rf automation/src/main/java/module/serviceUiIntegration
rm -rf automation/src/main/java/module/windowsApp
# remove tests
echo remove tests
rm -rf automation/src/test/java/test/module/web
rm -rf automation/src/test/java/test/module/ios
rm -rf automation/src/test/java/test/module/service
rm -rf automation/src/test/java/test/module/serviceIntegration
rm -rf automation/src/test/java/test/module/win
# remove suites
echo remove suites
rm -rf automation/suites/webSmokeTests.xml
rm -rf automation/suites/completeTestSuite.xml
rm -rf automation/suites/iosSmokeTests.xml
rm -rf automation/suites/servicesTests.xml
rm -rf automation/suites/serviceIntegration.xml
rm -rf automation/suites/winSmokeTests.xml
# remove resources
echo remove resources
rm -rf automation/resources/selendroid.apk

# remove runner 
echo remove runner 
bash runner/generateScripts.sh

# add to zip
cd ../
zip autonomx-android-$VERSION.zip ./autonomx-android

# remove non zip project
rm -rf ./autonomx-android











