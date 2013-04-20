@echo off


SET ROOT=%~dp0..
SET JARDIR=%ROOT%\lib
SET TEMPLATE_DIR=%ROOT%\war\WEB-INF\templates

REM Build java classpath:
SET META_GEN_CLASSPATH=%META_GEN_CLASSPATH%;%ROOT%\classes
SET META_GEN_CLASSPATH=%META_GEN_CLASSPATH%;%JARDIR%\util.jar
SET META_GEN_CLASSPATH=%META_GEN_CLASSPATH%;%JARDIR%\log4j.jar

REM ready to go:
REM echo would run: 
@java -classpath %META_GEN_CLASSPATH% fi.tnie.db.build.Builder --help

@echo on