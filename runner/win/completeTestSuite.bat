cd ../../automation
 ../runner/utils/mvn/bin/mvn clean compiler:compile compiler:testCompile surefire:test -Dsurefire.suiteXmlFiles=suites/completeTestSuite.xml