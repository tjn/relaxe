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

JARDIR=$ROOT/lib
TEMPLATE_DIR=$ROOT/war/WEB-INF/templates

META_GEN_CLASSPATH=$META_GEN_CLASSPATH:$ROOT/classes
META_GEN_CLASSPATH=$META_GEN_CLASSPATH:$JARDIR/util.jar
META_GEN_CLASSPATH=$META_GEN_CLASSPATH:$JARDIR/slf4j-api-1.7.5.jar
META_GEN_CLASSPATH=$META_GEN_CLASSPATH:$JARDIR/log4j-1.2.17.jar
META_GEN_CLASSPATH=$META_GEN_CLASSPATH:$JARDIR/slf4j-log4j12-1.7.5.jar


ROOT_PACKAGE=com.appspot.relaxe.gen.$IMPTAG.$TARGET.ent
CC_PACKAGE=com.appspot.relaxe.$TARGET.genctx
GENSRC=$ROOT/impl/$IMPTAG/$TARGET/src/out

# echo gensr $GENSRC
echo $META_GEN_CLASSPATH

# generate sources
java -classpath $META_GEN_CLASSPATH com.appspot.relaxe.build.Builder -g $GENSRC -t $TEMPLATE_DIR -j $JDBC_CONFIG -e $ENV --root-package $ROOT_PACKAGE  --type-mapper-implementation $TYPE_MAPPER_IMPLEMENTATION --catalog-context-package $CC_PACKAGE -u $JDBC_URL > log/generate-$IMPTAG-$TARGET.log

echo result $?
