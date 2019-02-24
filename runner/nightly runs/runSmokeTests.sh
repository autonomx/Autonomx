#!/bin/bash

export PATH="/usr/local/bin:$PATH"

# pull automation code
cd ../../; git clean  -d  -f""; git reset --hard; git checkout master; git pull --rebase; git fetch -p; 

# run selenium tests
cd selenium
../runner/utils/mvn/bin/mvn clean compiler:compile compiler:testCompile surefire:test -PcompleteTestSuite -DenableEmailReport=true -DlaunchReportAfterTest=false -DandroidHome=/Users/$USER/Library/Android/sdk