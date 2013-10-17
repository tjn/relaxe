@echo off

SET ROOT=%~dp0..
SET IMPDIR=pg
SET JDBC_CONFIG=%ROOT%\config\%IMPDIR%\%TARGET%.properties
SET ENV=com.appspot.relaxe.meta.impl.pg.PGImplementation

echo %TARGET%

SET META_GEN_CLASSPATH=%ROOT%\lib\postgresql-9.2-1002.jdbc4.jar

generate
@echo on