#!/bin/sh

ROOT=$(dirname $0)/..
JARDIR=$ROOT/lib

GENSRC=$ROOT/out/src

mkdir -p $ROOT/gen
mkdir -p $GENSRC

# JDBC_CONFIG=$ROOT/pg.properties
# JDBC_URL=jdbc:postgresql:test
# ENV=com.appspot.relaxe.env.pg.PGImplementation
# ROOT_PACKAGE=com.appspot.relaxe.gen.ent
# CC_PACKAGE=com.appspot.relaxe.genctx

# echo $JARDIR

# Build java classpath:
META_GEN_CP=$ROOT/classes
META_GEN_CP=$META_GEN_CP:$JARDIR/jdbc/postgresql/postgresql-9.3-1100-jdbc4.jar
META_GEN_CP=$META_GEN_CP:$JARDIR/util.jar
META_GEN_CP=$META_GEN_CP:$JARDIR/slf4j-api-1.7.5.jar
META_GEN_CP=$META_GEN_CP:$JARDIR/logback-core-1.0.13.jar
META_GEN_CP=$META_GEN_CP:$JARDIR/junit.jar

# REM ready to go:
java -classpath $META_GEN_CP com.appspot.relaxe.MyTestSuite
