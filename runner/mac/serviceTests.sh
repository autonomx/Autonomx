#!/bin/bash
 cd "$(dirname ${BASH_SOURCE[0]})"
 cd ../../automation 
 if [ ! -f .maven/maven.jar ]; then 
   bash ../setup.sh 
 fi 
 java -jar ./.maven/maven.jar clean compile test -DsuiteXmlFile=suites/serviceTests.xml 
 $SHELL