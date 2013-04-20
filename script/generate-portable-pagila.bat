@echo off

SET JDBC_URL=jdbc:postgresql:pagila
SET TARGET=pp
REM SET BUILDER_OPTS=--schema public -o public
SET BUILDER_OPTS=--environment-implementation fi.tnie.db.meta.PortableEnvironment
SET TYPE_MAPPER_IMPLEMENTATION=fi.tnie.db.test.PagilaTypeMapper

generate-pg
@echo on