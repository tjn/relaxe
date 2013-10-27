#!/bin/sh

echo $TARGET

ROOT=$(dirname $0)/..
IMPTAG=pg
JDBC_CONFIG=$ROOT/config/$IMPTAG/$TARGET.properties
ENV=com.appspot.relaxe.env.pg.PGImplementation
META_GEN_CLASSPATH=$ROOT/lib/postgresql-9.2-1002.jdbc4.jar

. generate.sh

