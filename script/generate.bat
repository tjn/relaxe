@echo off

if "%IMPTAG%" == "" GOTO :no_imp_tag

if "%TARGET%" == "" GOTO :no_target

if "%TYPE_MAPPER_IMPLEMENTATION%" == "" GOTO :set_default_type_mapper

:type_mapper_set

if "%ROOT_PACKAGE%" == "" GOTO :set_default_root_package

echo JDBC_URL=%JDBC_URL%

:root_package_set

REM tstamp

SET ROOT=%~dp0..
SET JARDIR=%ROOT%\lib
SET TEMPLATE_DIR=%ROOT%\war\WEB-INF\templates

SET ROOT_PACKAGE=com.appspot.relaxe.gen.%IMPTAG%.%TARGET%.ent
SET CC_PACKAGE=com.appspot.relaxe.%TARGET%.genctx
SET GENSRC=%ROOT%\impl\%IMPTAG%\%TARGET%\src\out

MKDIR %ROOT%\gen 2> nul
MKDIR %GENSRC% 2> nul

REM echo %JARDIR%

REM Build java classpath:
SET META_GEN_CLASSPATH=%META_GEN_CLASSPATH%;%ROOT%\classes
SET META_GEN_CLASSPATH=%META_GEN_CLASSPATH%;%JARDIR%\util.jar
SET META_GEN_CLASSPATH=%META_GEN_CLASSPATH%;%JARDIR%\slf4j-api-1.7.5.jar
REM SET META_GEN_CLASSPATH=%META_GEN_CLASSPATH%;%JARDIR%\logback-core-1.0.13.jar
SET META_GEN_CLASSPATH=%META_GEN_CLASSPATH%;%JARDIR%\log4j-1.2.17.jar
SET META_GEN_CLASSPATH=%META_GEN_CLASSPATH%;%JARDIR%\slf4j-log4j12-1.7.5.jar

REM ready to go:
REM echo would run: 
java -classpath %META_GEN_CLASSPATH% com.appspot.relaxe.build.Builder -g %GENSRC% -t %TEMPLATE_DIR% -j %JDBC_CONFIG% -e %ENV% --root-package %ROOT_PACKAGE% --type-mapper-implementation %TYPE_MAPPER_IMPLEMENTATION% --catalog-context-package %CC_PACKAGE% %BUILDER_OPTS% -u %JDBC_URL% || echo generation failed
GOTO :finally

:no_target
echo Undefined environment variable: TARGET
GOTO :finally

:no_imp_tag
echo Undefined environment variable: IMPTAG
GOTO :finally


:set_default_type_mapper
SET TYPE_MAPPER_IMPLEMENTATION=com.appspot.relaxe.DefaultTypeMapper
echo No type mapper set, using default: %TYPE_MAPPER_IMPLEMENTATION%
GOTO :type_mapper_set


:set_default_root_package
SET ROOT_PACKAGE=com.appspot.relaxe.gen.%IMPTAG%.%TARGET%.ent
echo No root package set, using default: %ROOT_PACKAGE%
GOTO :root_package_set


:finally
@echo on