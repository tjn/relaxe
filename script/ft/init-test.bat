@echo off
CALL init.bat

echo JARDIR=%JARDIR%
ls %JARDIR%

REM Build java classpath:
SET META_GEN_CP=%ROOT%\classes
SET META_GEN_CP=%META_GEN_CP%;%JARDIR%\util.jar
SET META_GEN_CP=%META_GEN_CP%;%JARDIR%\log4j.jar

@echo on