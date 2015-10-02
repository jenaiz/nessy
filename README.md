Nessy server
====

Nessy is an experimental cluster of nodes. There are to type of roles: root and chunker. The first one is the master and
the node that control everything. It has a connection with the other nodes. The chunkers connect with the root if it
is available or try it forever if not.

How to run the nodes:

There is a  script to start nodes, you only need to say with json file you want to use. For example if you want to run
the root node, you only need to do:

./start.sh root

For the normal nodes, is the same:

./start.sh chunker1
...
./start.sh chunker5

with that you will have a cluster of 5 chunkers with one root.
---
mvn clean install

to get the lib folder with the jars


./start.sh ???
./stop.sh ???


./chunker?.sh -> run a worker...
---
Copyright (c) 2014-2015 Jesús Navarrete jesus.navarrete@gmail.com, released under the GPLv2 license