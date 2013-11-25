@echo off
SET ROOT=%~dp0..\..
SET JARDIR=%ROOT%\lib

REM Build java classpath:
SET META_GEN_CP=%ROOT%\classes
SET META_GEN_CP=%META_GEN_CP%;%JARDIR%\util.jar
SET META_GEN_CP=%META_GEN_CP%;%JARDIR%\slf4j-api-1.7.5.jar
SET META_GEN_CP=%META_GEN_CP%;%JARDIR%\logback-core-1.0.13.jar


@echo on