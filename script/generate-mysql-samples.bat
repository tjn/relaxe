@echo off

SET TARGET=samples
SET BUILDER_OPTS=--schema %TARGET% -o %TARGET%
SET JDBC_URL=jdbc:mysql://127.0.0.1/samples

generate-mysql
@echo on