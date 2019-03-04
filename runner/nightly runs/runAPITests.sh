#!/bin/bash

export PATH="/usr/local/bin:$PATH"

# pull code
cd ../../; git clean  -d  -f""; git reset --hard; git checkout master; git pull --rebase; git fetch -p; 

# run tests
cd automation
../runner/utils/mvn/bin/mvn clean compiler:compile compiler:testCompile surefire:test -PservicesSmokeTests -DenableEmailReport=true -DlaunchReportAfterTest=false -DandroidHome=/Users/$USER/Library/Android/sdk