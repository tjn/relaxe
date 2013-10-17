#!/bin/sh

if [ "$TARGET" == "" ]
then 
  echo no target
  exit -1
fi

if [ "$$TYPE_MAPPER_IMPLEMENTATION" == "" ]
then
  TYPE_MAPPER_IMPLEMENTATION=com.appspot.relaxe.DefaultTypeMapper
  echo No type mapper set, using default: $TYPE_MAPPER_IMPLEMENTATION
fi

# tstamp

META_GEN_CLASSPATH=$META_GEN_CLASSPATH:$ROOT/classes
META_GEN_CLASSPATH=$META_GEN_CLASSPATH:$JARDIR/util.jar
META_GEN_CLASSPATH=$META_GEN_CLASSPATH:$JARDIR/log4j.jar

# generate sources
java -classpath $META_GEN_CLASSPATH com.appspot.relaxe.build.Builder -g $GENSRC -t $TEMPLATE_DIR -j $JDBC_CONFIG -e $ENV --root-package $ROOT_PACKAGE  --type-mapper-implementation $TYPE_MAPPER_IMPLEMENTATION --catalog-context-package $CC_PACKAGE -u $JDBC_URL > log/generate-$TARGET.log

echo result $?
