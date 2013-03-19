@echo off

REM tstamp

SET ROOT=%~dp0..
SET TARGET=sakila
SET JDBC_CONFIG=%ROOT%\%TARGET%.properties
SET ENV=fi.tnie.db.env.mysql.MySQLImplementation

SET META_GEN_CLASSPATH=%ROOT%\lib\mysql-connector-java-5.1.11-bin.jar

generate
@echo on