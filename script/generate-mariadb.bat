@echo off

REM tstamp

SET ROOT=%~dp0..
SET IMPTAG=mariadb
SET JDBC_CONFIG=%ROOT%\config\%IMPTAG%\%TARGET%.properties
SET ENV=com.appspot.relaxe.env.mariadb.MariaDBImplementation

SET META_GEN_CLASSPATH=%ROOT%\lib\mariadb-java-client-1.1.5.jar

generate
@echo on