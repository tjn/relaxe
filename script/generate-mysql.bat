@echo off

REM tstamp

SET ROOT=%~dp0..
SET IMPTAG=mysql
SET JDBC_CONFIG=%ROOT%\config\%IMPTAG%\%TARGET%.properties
SET ENV=com.appspot.relaxe.env.mysql.MySQLImplementation

SET META_GEN_CLASSPATH=%ROOT%\lib\mysql-connector-java-5.1.18-bin.jar

generate
@echo on