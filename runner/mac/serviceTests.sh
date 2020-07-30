#!/bin/bash
 cd "$(dirname ${BASH_SOURCE[0]})"
 cd ../../automation
 mvn clean compile test -DsuiteXmlFile=suites/serviceTests.xml