#! /bin/bash

export JAVA_OPTS_NESSY="-Xms128m -Xmx256m -XX:PermSize=64m -XX:MaxPermSize=128m"

export CLASSPATH=".";
for i in ./lib/*.jar ; do
  CLASSPATH=$CLASSPATH:$i
done

CLASSPATH=$CLASSPATH:target/classes/:../../resources
echo $CLASSPATH

java $JAVA_OPTS_NESSY -classpath $CLASSPATH org.alblang.server.Kernel root.json
