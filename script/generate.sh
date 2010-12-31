#!/bin/sh

tstamp
ROOT=$(dirname $0)/..
JARDIR=$ROOT/lib

GENSRC=$ROOT/out/src

mkdir $ROOT/gen
mkdir $GENSRC

JDBC_CONFIG=$ROOT/pg.properties
JDBC_URL=jdbc:postgresql:test
ENV=fi.tnie.db.env.pg.PGImplementation
ROOT_PACKAGE=fi.tnie.db.gen.ent
CC_PACKAGE=fi.tnie.db.genctx

# echo $JARDIR

# Build java classpath:
META_GEN_CP=$ROOT/classes
META_GEN_CP=$META_GEN_CP:$JARDIR/postgresql-8.4-701.jdbc3.jar
META_GEN_CP=$META_GEN_CP:$JARDIR/util.jar
META_GEN_CP=$META_GEN_CP:$JARDIR/log4j.jar

# REM ready to go:
java -classpath $META_GEN_CP fi.tnie.db.build.Builder -g $GENSRC -j $JDBC_CONFIG -e $ENV --root-package $ROOT_PACKAGE --catalog-context-package $CC_PACKAGE -u $JDBC_URL || echo generation failed