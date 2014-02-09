#!/bin/sh

echo $TARGET

ROOT=$(dirname $0)/..
IMPTAG=pg
JDBC_CONFIG=$ROOT/config/$IMPTAG/$TARGET.properties
ENV=com.appspot.relaxe.rdbms.pg.PGImplementation
META_GEN_CLASSPATH=$ROOT/lib/jdbc/postgresql/postgresql-9.3-1100-jdbc4.jar

. generate.sh

