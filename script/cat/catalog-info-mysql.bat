@echo off
SET HOME=%~dp0
SET ROOT=%HOME%..\..
SET JARDIR=%ROOT%\lib

SET JDBC_CONFIG=%ROOT%\mysql.properties
SET JDBC_URL=jdbc:mysql://127.0.0.1/mysql

REM Build java classpath:
SET META_GEN_CP=%HOME%
SET META_GEN_CP=%META_GEN_CP%;%ROOT%\classes
SET META_GEN_CP=%META_GEN_CP%;%JARDIR%\mysql-connector-java-5.1.11-bin.jar
SET META_GEN_CP=%META_GEN_CP%;%JARDIR%\util.jar
SET META_GEN_CP=%META_GEN_CP%;%JARDIR%\log4j.jar

REM read to go:
java -classpath %META_GEN_CP% com.appspot.relaxe.tools.CatalogInfo -v -j %JDBC_CONFIG% -u %JDBC_URL%
@echo on