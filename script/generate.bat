@echo off
SET ROOT=%~dp0..
SET JARDIR=%ROOT%\lib

SET GENSRC=%ROOT%\out\src
REM SET GENSRC=C:\Users\tnie\devel\project\webui\src

MKDIR %ROOT%\gen 2> NULL
MKDIR %GENSRC% 2> NULL

SET JDBC_CONFIG=%ROOT%\pg.properties
SET JDBC_URL=jdbc:postgresql:test
SET ENV=fi.tnie.db.env.pg.PGImplementation
SET ROOT_PACKAGE=fi.tnie.db.gen
SET CC_PACKAGE=fi.tnie.db.genctx

REM echo %JARDIR%

REM Build java classpath:
SET META_GEN_CP=%ROOT%\classes
SET META_GEN_CP=%META_GEN_CP%;%JARDIR%\postgresql-8.4-701.jdbc3.jar
SET META_GEN_CP=%META_GEN_CP%;%JARDIR%\util.jar
SET META_GEN_CP=%META_GEN_CP%;%JARDIR%\log4j.jar

REM read to go:
java -classpath %META_GEN_CP% fi.tnie.db.build.Builder -g %GENSRC% -j %JDBC_CONFIG% -e %ENV% --root-package %ROOT_PACKAGE% --catalog-context-package %CC_PACKAGE% -u %JDBC_URL%
@echo on