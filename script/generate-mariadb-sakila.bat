@echo off

SET TARGET=sakila
SET BUILDER_OPTS=--schema %TARGET% -o %TARGET%
SET JDBC_URL=jdbc:mariadb://127.0.0.1:3307/%TARGET%

generate-mariadb
@echo on