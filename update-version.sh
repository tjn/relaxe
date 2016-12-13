#!/bin/sh

if git describe HEAD 2> /dev/null
then
  export RELAXE_VERSION=$(git describe HEAD)
  
else
  export RELAXE_VERSION=$(git log -n1 --pretty=format:'[%H %d]')
fi

echo relaxe.revision=$RELAXE_VERSION > version.properties
# echo relaxe.revision=$(git describe HEAD) > version.properties