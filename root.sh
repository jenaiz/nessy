#! /bin/bash

export JAVA_OPTS_NESSY="-Xms128m -Xmx256m -XX:PermSize=64m -XX:MaxPermSize=128m"

export CLASSPATH=".";
for i in ./lib/*.jar ; do
  CLASSPATH=$CLASSPATH:$i
done

CLASSPATH=$CLASSPATH:target/classes/:../../resources

java $JAVA_OPTS_NESSY -classpath $CLASSPATH org.alblang.nessy.server.Kernel root.json -Dlog4j.configuration=file:src/resources/log4j.properties
