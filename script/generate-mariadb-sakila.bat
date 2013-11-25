@echo off

SET TARGET=sakila
SET BUILDER_OPTS=--schema %TARGET% -o %TARGET%
SET JDBC_URL=jdbc:mariadb://127.0.0.1:3307/%TARGET%
REM SET BUILDER_OPTS=--environment-implementation com.appspot.relaxe.pg.pagila.types.PagilaEnvironment

generate-mariadb
@echo on