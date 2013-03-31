@echo off

SET JARDIR=..\lib

SET RLX_CP=..\test
SET RLX_CP=%RLX_CP%;..\classes
SET RLX_CP=%RLX_CP%;%JARDIR%\junit.jar
SET RLX_CP=%RLX_CP%;%JARDIR%\log4j.jar
SET RLX_CP=%RLX_CP%;%JARDIR%\postgresql-9.2-1002.jdbc4.jar
SET RLX_CP=%RLX_CP%;%JARDIR%\mysql-connector-java-5.1.18-bin.jar
SET RLX_CP=%RLX_CP%;%JARDIR%\hsqldb.jar

@echo %1
java -cp %RLX_CP% junit.textui.TestRunner %1

if ERRORLEVEL 1 exit /B %ERRORLEVEL%

@echo on