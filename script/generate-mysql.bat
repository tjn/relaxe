@echo off

REM tstamp

SET ROOT=%~dp0..
SET IMPTAG=mysql
SET JDBC_CONFIG=%ROOT%\config\%IMPTAG%\%TARGET%.properties
SET ENV=com.appspot.relaxe.rdbms.mysql.MySQLImplementation

SET META_GEN_CLASSPATH=%ROOT%\lib\jdbc\mysql-connector-java\mysql-connector-java-5.1.27.jar

generate
@echo on