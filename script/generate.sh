#!/bin/sh

if [ "$TARGET" == "" ]
then 
  echo no target
  exit -1
fi

# tstamp

META_GEN_CLASSPATH=$META_GEN_CLASSPATH:$ROOT/classes
META_GEN_CLASSPATH=$META_GEN_CLASSPATH:$JARDIR/util.jar
META_GEN_CLASSPATH=$META_GEN_CLASSPATH:$JARDIR/log4j.jar

# generate sources
java -classpath $META_GEN_CLASSPATH fi.tnie.db.build.Builder -g $GENSRC -t $TEMPLATE_DIR -j $JDBC_CONFIG -e $ENV --root-package $ROOT_PACKAGE --catalog-context-package $CC_PACKAGE -u $JDBC_URL > log/generate-$TARGET.log || echo generation failed
