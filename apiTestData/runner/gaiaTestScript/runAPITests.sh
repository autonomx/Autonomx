#!/bin/bash

export PATH="/usr/local/bin:$PATH"

# pull gaia automation code
cd gaia-automation; git clean  -d  -f""; git reset --hard; git checkout master; git pull --rebase; git fetch -p; 

# run selenium tests
cd selenium
mvn clean compiler:compile compiler:testCompile surefire:test -PapiTestRunner -DbrowserType=CHROME 