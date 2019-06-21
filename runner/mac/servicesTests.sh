#!/bin/bash
 cd "$(dirname ${BASH_SOURCE[0]})"
 cd ../../automation
 ../runner/utils/mvn/bin/mvn clean compile test -DsuiteXmlFile=suites/servicesTests.xml