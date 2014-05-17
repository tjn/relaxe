#!/bin/sh

find src -name '*.java' |xargs -n1 grep -L Copyright
find impl -name '*.java' |xargs -n1 grep -L Copyright

