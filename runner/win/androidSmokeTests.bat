cd ../../selenium
 mvn clean compiler:compile compiler:testCompile surefire:test -Dsurefire.suiteXmlFiles=suites/androidSmokeTests.xml