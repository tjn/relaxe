@echo off

SET JDBC_URL=jdbc:postgresql:pagila
SET TARGET=pagila
REM SET BUILDER_OPTS=--schema public -o public
REM SET BUILDER_OPTS=
SET BUILDER_OPTS=--environment-implementation com.appspot.relaxe.pg.pagila.types.PagilaEnvironment
SET TYPE_MAPPER_IMPLEMENTATION=com.appspot.relaxe.pg.pagila.PagilaTypeMapper

generate-pg
@echo on