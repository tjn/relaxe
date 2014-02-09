@echo off

SET JDBC_URL=jdbc:postgresql:pagila
SET TARGET=pp
REM SET BUILDER_OPTS=--schema public -o public
SET BUILDER_OPTS=--environment-implementation com.appspot.relaxe.env.PortableEnvironment
SET TYPE_MAPPER_IMPLEMENTATION=com.appspot.relaxe.pg.pagila.PagilaTypeMapper

generate-pg
@echo on