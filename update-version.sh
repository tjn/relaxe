#!/bin/sh

if git describe HEAD 2> /dev/null
then
  echo relaxe.revision=$(git describe HEAD) > version.properties
else
  echo relaxe.revision=$(git log -n1 --pretty=format:'[%H %d]') > version.properties
fi