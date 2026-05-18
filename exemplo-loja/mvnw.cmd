@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF)
@REM Maven Wrapper startup batch script
@REM ----------------------------------------------------------------------------
@IF "%__MVNW_ARG0_NAME__%"=="" (SET __MVNW_ARG0_NAME__=%~nx0)
@SET ___MVNW_UGLY_PROXY=
@SETLOCAL
@SET MAVEN_PROJECTBASEDIR=%~dp0
IF NOT "%MAVEN_PROJECTBASEDIR:~-1%"=="\" SET MAVEN_PROJECTBASEDIR=%MAVEN_PROJECTBASEDIR%\
@echo off
SET WRAPPER_PROPERTIES=%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.properties
FOR /F "usebackq tokens=1,2 delims==" %%A IN ("%WRAPPER_PROPERTIES%") DO (
    IF "%%A"=="distributionUrl" SET DISTRIBUTION_URL=%%B
)
SET MAVEN_USER_HOME=%USERPROFILE%\.m2
SET M2_HOME=%MAVEN_USER_HOME%\wrapper\dists
FOR /F "tokens=*" %%i IN ('powershell -Command "[System.IO.Path]::GetFileNameWithoutExtension(\"apache-maven-3.9.6-bin.zip\")"') DO SET DIST_NAME=%%i
SET MAVEN_HOME=%M2_HOME%\apache-maven-3.9.6-bin\%DIST_NAME%
IF EXIST "%MAVEN_HOME%\bin\mvn.cmd" GOTO RUN_MAVEN
mkdir "%MAVEN_HOME%" 2>NUL
echo Baixando Apache Maven 3.9.6...
powershell -Command "Invoke-WebRequest -Uri '%DISTRIBUTION_URL%' -OutFile '%TEMP%\maven.zip'"
powershell -Command "Expand-Archive -Path '%TEMP%\maven.zip' -DestinationPath '%M2_HOME%\apache-maven-3.9.6-bin\'"
:RUN_MAVEN
SET PATH=%MAVEN_HOME%\bin;%PATH%
"%MAVEN_HOME%\bin\mvn.cmd" %*
