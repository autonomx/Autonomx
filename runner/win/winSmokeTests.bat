cd %~dp0
 cd ../../automation
 mvn clean compile test -DsuiteXmlFile=suites/winSmokeTests.xml