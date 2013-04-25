@echo off
CALL generate-pg-pagila
CALL generate-mysql-sakila
CALL generate-mysql-samples.bat
CALL generate-portable-pagila.bat
@echo on