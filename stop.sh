#! /bin/bash

node=$1

if [ -z "$node" ]; then
  node="root"
fi

kill -9 $(cat pids/$node.pid)
rm pids/$node.pid
