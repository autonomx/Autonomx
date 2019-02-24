#!/bin/bash -l
cd "${0%/*}"
cd ../../../automation
mvn clean compiler:compile compiler:testCompile surefire:test -Dsurefire.suiteXmlFiles=suites/servicesTests.xml -DlaunchReportAfterTest=true
