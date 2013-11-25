@echo off

SET ROOT=%~dp0..
SET IMPTAG=pg
SET JDBC_CONFIG=%ROOT%\config\%IMPTAG%\%TARGET%.properties
SET ENV=com.appspot.relaxe.meta.impl.pg.PGImplementation

echo %TARGET%

SET META_GEN_CLASSPATH=%ROOT%\lib\jdbc\postgresql\postgresql-9.3-1100-jdbc4.jar

generate
@echo on