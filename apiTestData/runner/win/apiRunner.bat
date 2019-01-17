cd ../../../selenium
mvn clean compiler:compile compiler:testCompile surefire:test -PapiTestRunner -DbrowserType=CHROME 