@echo off

SET JARDIR=..\lib

SET RLX_CP=..\test
SET RLX_CP=%RLX_CP%;..\classes
SET RLX_CP=%RLX_CP%;%JARDIR%\test\junit-4.11.jar
SET RLX_CP=%RLX_CP%;%JARDIR%\slf4j-api-1.7.5.jar
SET RLX_CP=%RLX_CP%;%JARDIR%\logback-core-1.0.13.jar
REM SET RLX_CP=%RLX_CP%;%JARDIR%\logback-core-1.0.13.jar
SET RLX_CP=%RLX_CP%;%JARDIR%\log4j-1.2.17.jar
SET RLX_CP=%RLX_CP%;%JARDIR%\slf4j-log4j12-1.7.5.jar


SET RLX_CP=%RLX_CP%;%JARDIR%\jdbc\postgresql\postgresql-9.3-1100-jdbc4.jar
SET RLX_CP=%RLX_CP%;%JARDIR%\jdbc\mysql-connector-java\mysql-connector-java-5.1.27.jar
SET RLX_CP=%RLX_CP%;%JARDIR%\jdbc\hsqldb\hsqldb-1.8.0.10.jar
SET RLX_CP=%RLX_CP%;%JARDIR%\jdbc\mariadb-java-client\mariadb-java-client-bzr-trunk-20131123.jar

@echo %1
java -cp %RLX_CP% junit.textui.TestRunner %1

if ERRORLEVEL 1 exit /B %ERRORLEVEL%

@echo on