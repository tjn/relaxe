@echo off

REM tstamp

SET ROOT=%~dp0..
REM SET TARGET=pg
SET IMPDIR=pg
SET JDBC_CONFIG=%ROOT%\config\%IMPDIR%\%TARGET%.properties
SET ENV=fi.tnie.db.env.pg.PGImplementation

echo %TARGET%

SET META_GEN_CLASSPATH=%ROOT%\lib\postgresql-9.2-1002.jdbc4.jar

generate
@echo on