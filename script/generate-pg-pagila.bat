@echo off

SET JDBC_URL=jdbc:postgresql:pagila
SET TARGET=pagila
REM SET BUILDER_OPTS=--schema public -o public
SET BUILDER_OPTS=
SET TYPE_MAPPER_IMPLEMENTATION=com.appspot.relaxe.pg.pagila.PagilaTypeMapper

generate-pg
@echo on