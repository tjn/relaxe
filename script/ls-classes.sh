

OUTPUT_DIR=../classes
TMP_DIR=../tmp
mkdir -p $TMP_DIR

A=$TMP_DIR/classes.1
B=$TMP_DIR/classes.2
C=$TMP_DIR/classes.3

# list all class files but ignore inner classes
find $OUTPUT_DIR -name "*.class" -a -not -name "*\$*" > $A
sed 's/^\.\.\/classes\///g' < $A > $B
sed 's/\//./g' < $B > $C
sed 's/\.class$//g' < $C > $OUTPUT_DIR/classlist
cat $OUTPUT_DIR/classlist

