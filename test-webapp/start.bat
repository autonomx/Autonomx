@echo off
setlocal
cd /d %~dp0
if not exist .build mkdir .build
javac -encoding UTF-8 -d .build FrameworkTestWebApp.java
if errorlevel 1 exit /b %errorlevel%
if "%AUTONOMX_TEST_WEBAPP_PORT%"=="" set AUTONOMX_TEST_WEBAPP_PORT=18080
java -cp .build FrameworkTestWebApp %AUTONOMX_TEST_WEBAPP_PORT%
