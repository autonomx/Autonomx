 cd %~dp0
 cd ../../automation
 if not exist .maven/maven.jar ( 
  call %~dp0../../setup.bat 
 ) 
 java -jar .maven/maven.jar clean compile test -DsuiteXmlFile=suites/winSmokeTests.xml 
 cmd /k