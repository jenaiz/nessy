#! /bin/bash

export JAVA_OPTS_NESSY="-Xms128m -Xmx256m -XX:PermSize=64m -XX:MaxPermSize=128m"

export CLASSPATH=".";
for i in ./lib/*.jar ; do
  CLASSPATH=$CLASSPATH:$i
done

node=$1

if [ -z "$node" ]; then
  node="root"
fi

CLASSPATH=$CLASSPATH:target/classes/

nohup java $JAVA_OPTS_NESSY -cp $CLASSPATH org.alblang.nessy.server.Kernel $node.json > logs/$node.log 2>&1 &
pid=$!
echo $pid >> pids/$node.pid

echo "$node is running with pid $pid"
#tail -f logs/$node.log
