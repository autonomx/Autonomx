#!/bin/bash
 cd "$(dirname ${BASH_SOURCE[0]})"
 cd ../../selenium
 mvn clean compiler:compile compiler:testCompile surefire:test -Dsurefire.suiteXmlFiles=suites/androidSmokeTests.xml