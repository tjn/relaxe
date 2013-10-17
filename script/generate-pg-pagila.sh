#!/bin/sh

JDBC_URL=jdbc:postgresql:pagila
TARGET=pagila
# SET BUILDER_OPTS=--schema public -o public
BUILDER_OPTS=
TYPE_MAPPER_IMPLEMENTATION=com.appspot.relaxe.pg.pagila.PagilaTypeMapper

. generate-pg.sh
