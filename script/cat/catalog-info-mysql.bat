@echo off
SET HOME=%~dp0
SET ROOT=%HOME%..\..
SET JARDIR=%ROOT%\lib

SET JDBC_CONFIG=%ROOT%\mysql.properties
SET JDBC_URL=jdbc:mysql://127.0.0.1/mysql

REM Build java classpath:
SET META_GEN_CP=%HOME%
SET META_GEN_CP=%META_GEN_CP%;%ROOT%\classes
SET META_GEN_CP=%META_GEN_CP%;%JARDIR%\lib\jdbc\mysql-connector-java\mysql-connector-java-5.1.27.jar
SET META_GEN_CP=%META_GEN_CP%;%JARDIR%\util.jar
SET META_GEN_CP=%META_GEN_CP%;%JARDIR%\slf4j-api-1.7.5.jar
SET META_GEN_CP=%META_GEN_CP%;%JARDIR%\logback-core-1.0.13.jar


REM read to go:
java -classpath %META_GEN_CP% com.appspot.relaxe.tools.CatalogInfo -v -j %JDBC_CONFIG% -u %JDBC_URL%
@echo on