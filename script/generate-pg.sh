#!/bin/sh

ROOT=$(dirname $0)/..
TARGET=pg
JARDIR=$ROOT/lib
GENSRC=$ROOT/out/src

mkdir -p $ROOT/gen
mkdir -p $GENSRC

JDBC_CONFIG=$ROOT/pg.properties
JDBC_URL=jdbc:postgresql:test
ENV=fi.tnie.db.env.pg.PGImplementation
ROOT_PACKAGE=fi.tnie.db.gen.ent
CC_PACKAGE=fi.tnie.db.genctx

# echo $JARDIR

# Build java classpath:
META_GEN_CLASSPATH=$ROOT/classes
META_GEN_CLASSPATH=$META_GEN_CLASSPATH:$JARDIR/postgresql-8.4-701.jdbc3.jar
META_GEN_CLASSPATH=$META_GEN_CLASSPATH:$JARDIR/util.jar
META_GEN_CLASSPATH=$META_GEN_CLASSPATH:$JARDIR/log4j.jar

. generate.sh

