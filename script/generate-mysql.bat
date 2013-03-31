@echo off

REM tstamp

SET ROOT=%~dp0..
SET IMPDIR=mysql
SET JDBC_CONFIG=%ROOT%\config\%IMPDIR%\%TARGET%.properties
SET ENV=fi.tnie.db.env.mysql.MySQLImplementation

SET META_GEN_CLASSPATH=%ROOT%\lib\mysql-connector-java-5.1.18-bin.jar

generate
@echo on