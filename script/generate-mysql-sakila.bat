@echo off

SET TARGET=sakila
SET BUILDER_OPTS=--schema %TARGET% -o %TARGET%
SET JDBC_URL=jdbc:mysql://127.0.0.1/%TARGET%

generate-mysql
@echo on