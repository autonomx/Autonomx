#!/bin/bash
 cd "$(dirname ${BASH_SOURCE[0]})"
 cd ../../automation/library
 java -jar mavenRunner.jar clean compile test -DsuiteXmlFile=suites/serviceIntegration.xml