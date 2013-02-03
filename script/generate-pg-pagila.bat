@echo off

SET JDBC_URL=jdbc:postgresql:pagila
REM SET TYPE_MAPPER_IMPLEMENTATION=fi.tnie.db.DefaultTypeMapper
SET TYPE_MAPPER_IMPLEMENTATION=fi.tnie.db.test.PagilaTypeMapper


generate-pg
@echo on