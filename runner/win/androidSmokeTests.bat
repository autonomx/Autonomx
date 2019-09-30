cd %~dp0
 cd ../../automation/library
 java -jar mavenRunner.jar clean compile test -DsuiteXmlFile=suites/androidSmokeTests.xml