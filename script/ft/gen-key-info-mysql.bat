@echo off
SET ROOT=%~dp0..\..
SET JARDIR=%ROOT%\lib

SET JDBC_CONFIG=%ROOT%\mysql.properties
SET JDBC_URL=jdbc:mysql://127.0.0.1/mysql
SET ENV=com.appspot.relaxe.rdbms.mysql.MySQLEnvironment

REM Build java classpath:
SET META_GEN_CP=%ROOT%\classes
SET META_GEN_CP=%META_GEN_CP%;%JARDIR%\lib\jdbc\mysql-connector-java\mysql-connector-java-5.1.27.jar
SET META_GEN_CP=%META_GEN_CP%;%JARDIR%\util.jar
SET META_GEN_CP=%META_GEN_CP%;%JARDIR%\slf4j-api-1.7.5.jar
SET META_GEN_CP=%META_GEN_CP%;%JARDIR%\logback-core-1.0.13.jar


REM read to go:
java -classpath %META_GEN_CP% com.appspot.relaxe.tools.KeyGenerationInfo -j %JDBC_CONFIG% -e %ENV% -u %JDBC_URL%
@echo on