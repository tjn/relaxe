@echo off

if "%TARGET%" == "" GOTO :no_target

tstamp

SET ROOT=%~dp0..
SET JARDIR=%ROOT%\lib
SET TEMPLATE_DIR=%ROOT%\war\WEB-INF\templates

SET ROOT_PACKAGE=fi.tnie.db.gen.%TARGET%.ent
REM SET CC_PACKAGE=fi.tnie.db.%TARGET%.genctx
SET CC_PACKAGE=fi.tnie.db.%TARGET%.genctx
SET GENSRC=%ROOT%\out\%TARGET%\src

MKDIR %ROOT%\gen 2> nul
MKDIR %GENSRC% 2> nul

REM echo %JARDIR%

REM Build java classpath:
SET META_GEN_CLASSPATH=%META_GEN_CLASSPATH%;%ROOT%\classes
SET META_GEN_CLASSPATH=%META_GEN_CLASSPATH%;%JARDIR%\util.jar
SET META_GEN_CLASSPATH=%META_GEN_CLASSPATH%;%JARDIR%\log4j.jar

REM ready to go:
java -classpath %META_GEN_CLASSPATH% fi.tnie.db.build.Builder -g %GENSRC% -t %TEMPLATE_DIR% -j %JDBC_CONFIG% -e %ENV% --root-package %ROOT_PACKAGE% --catalog-context-package %CC_PACKAGE% -u %JDBC_URL% || echo generation failed
GOTO :finally

:no_target
echo Undefined environment variable: TARGET


:finally
@echo on