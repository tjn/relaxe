@echo off

SET JDBC_URL=jdbc:mysql://127.0.0.1/sakila
REM SET TYPE_MAPPER_IMPLEMENTATION=fi.tnie.db.DefaultTypeMapper
REM SET TYPE_MAPPER_IMPLEMENTATION=fi.tnie.db.test.SakilaTypeMapper

generate-mysql
@echo on