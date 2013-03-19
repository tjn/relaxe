@echo off

if "%TARGET%" == "" GOTO :no_target

if "%TYPE_MAPPER_IMPLEMENTATION%" == "" GOTO :set_default_type_mapper

:generate

REM tstamp

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
REM echo would run: 
java -classpath %META_GEN_CLASSPATH% fi.tnie.db.build.Builder -g %GENSRC% -t %TEMPLATE_DIR% -j %JDBC_CONFIG% -e %ENV% --root-package %ROOT_PACKAGE% --type-mapper-implementation %TYPE_MAPPER_IMPLEMENTATION% --catalog-context-package %CC_PACKAGE% -u %JDBC_URL% || echo generation failed
GOTO :finally

:no_target
echo Undefined environment variable: TARGET
GOTO :finally

:set_default_type_mapper
SET TYPE_MAPPER_IMPLEMENTATION=fi.tnie.db.DefaultTypeMapper
echo No type mapper set, using default: %TYPE_MAPPER_IMPLEMENTATION%
GOTO :generate


:finally
@echo on