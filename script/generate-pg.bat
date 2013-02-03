@echo off

tstamp

SET ROOT=%~dp0..
SET TARGET=pg
SET JDBC_CONFIG=%ROOT%\%TARGET%.properties
SET ENV=fi.tnie.db.env.pg.PGImplementation

SET META_GEN_CLASSPATH=%ROOT%\lib\postgresql-8.4-701.jdbc3.jar

generate
@echo on