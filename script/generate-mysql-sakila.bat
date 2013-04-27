@echo off

SET TARGET=sakila
SET BUILDER_OPTS=--schema %TARGET% -o %TARGET%
SET JDBC_URL=jdbc:mysql://127.0.0.1/%TARGET%
REM SET TYPE_MAPPER_IMPLEMENTATION=com.appspot.relaxe.DefaultTypeMapper
REM SET TYPE_MAPPER_IMPLEMENTATION=com.appspot.relaxe.pg.pagila.SakilaTypeMapper

generate-mysql
@echo on