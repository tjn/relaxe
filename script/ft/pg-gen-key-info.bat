@echo off
SET ROOT=%~dp0..
SET JARDIR=%ROOT%\lib

SET JDBC_CONFIG=%ROOT%\pg.properties
SET JDBC_URL=jdbc:postgresql:test
SET ENV=com.appspot.relaxe.env.pg.PGImplementation

REM Build java classpath:
SET META_GEN_CP=%ROOT%\classes
SET META_GEN_CP=%META_GEN_CP%;%JARDIR%\postgresql-8.4-701.jdbc3.jar
SET META_GEN_CP=%META_GEN_CP%;%JARDIR%\util.jar
SET META_GEN_CP=%META_GEN_CP%;%JARDIR%\slf4j-api-1.7.5.jar
SET META_GEN_CP=%META_GEN_CP%;%JARDIR%\logback-core-1.0.13.jar


REM read to go:
java -classpath %META_GEN_CP% com.appspot.relaxe.tools.KeyGenerationInfo -j %JDBC_CONFIG% -e %ENV% -u %JDBC_URL%
@echo on