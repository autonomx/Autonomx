#!/bin/bash
 cd "$(dirname ${BASH_SOURCE[0]})"
 cd ../../automation
 ../runner/utils/mvn/bin/mvn clean compiler:compile compiler:testCompile surefire:test -Dsurefire.suiteXmlFiles=suites/restTests.xml