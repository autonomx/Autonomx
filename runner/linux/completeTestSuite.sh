#!/bin/bash
 cd "$(dirname ${BASH_SOURCE[0]})"
 cd ../../selenium
 ../runner/utils/mvn/bin/mvn clean compiler:compile compiler:testCompile surefire:test -Dsurefire.suiteXmlFiles=suites/completeTestSuite.xml