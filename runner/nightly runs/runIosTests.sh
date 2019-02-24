#!/bin/bash

export PATH="/usr/local/bin:$PATH"

# pull automation code
cd ../../; git clean  -d  -f""; git reset --hard; git checkout master; git pull --rebase; git fetch -p; 

# pull the latest dev
cd ../ipad; git clean  -d  -f""; git reset --hard; git checkout dev; git pull --rebase; git fetch -p; 

# install pods
pod install

# build project
xcodebuild -sdk iphonesimulator11.4 -workspace Gaia.xcworkspace -scheme Gaia -derivedDataPath './output'

# copy app file to root directory
cp -a ../ipad/output/Build/Products/Debug-iphonesimulator/Gaia.app/. ../gaia-automation/selenium/resources/Gaia.app

# kill simulator if open
#killall "Simulator" 2> /dev/null; xcrun simctl erase all
#sleep 5s

# run selenium tests
cd ../automation-client/selenium
../runner/utils/mvn/bin/mvn clean compile compiler:testCompile surefire:test -PiosSmokeTests