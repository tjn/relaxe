@echo off
SET ROOT=%~dp0..\..
SET JARDIR=%ROOT%\lib

SET JDBC_CONFIG=%ROOT%\pg.properties
SET JDBC_URL=jdbc:postgresql:test
SET ENV=fi.tnie.db.env.pg.PGImplementation

REM Build java classpath:
SET META_GEN_CP=%ROOT%\classes
SET META_GEN_CP=%META_GEN_CP%;%JARDIR%\util.jar
SET META_GEN_CP=%META_GEN_CP%;%JARDIR%\log4j.jar

SET META_GEN_CP=%META_GEN_CP%;%JARDIR%\postgresql-8.4-701.jdbc3.jar

REM read to go:
java -classpath %META_GEN_CP% fi.tnie.dbmeta.tools.KeyGenerationInfo -j %JDBC_CONFIG% -e %ENV% -u %JDBC_URL%
@echo on