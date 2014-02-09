@echo off

REM tstamp

SET ROOT=%~dp0..
SET IMPTAG=mariadb
SET JDBC_CONFIG=%ROOT%\config\%IMPTAG%\%TARGET%.properties
SET ENV=com.appspot.relaxe.rdbms.mariadb.MariaDBImplementation

REM SET META_GEN_CLASSPATH=%ROOT%\lib\jdbc\mariadb-java-client\mariadb-java-client-bzr-trunk-20131123.jar
REM SET META_GEN_CLASSPATH=%ROOT%\lib\jdbc\mariadb-java-client\mariadb-java-client-1.1.5.jar
SET META_GEN_CLASSPATH=%ROOT%\lib\jdbc\mariadb-java-client\mariadb-java-client-bzr-rev492.jar

generate
@echo on