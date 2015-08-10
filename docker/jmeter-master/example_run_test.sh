#!/bin/bash

remote='172.31.37.172,172.31.2.96,172.31.42.20'

# if parameter are passed overwrite default IPs
if ! [ -z "$1" ]
then
  remote=$1
fi

# start the run
./ssh_start_server.sh -k jmeter_master_to_server -d ${JMETER_HOME}/jmeter/data -t ${JMETER_HOME}/jmeter/neo4jTest.jmx  -R $remote

# start the analyse
cd ../jmeter-analyse
echo ""
echo "Starting analyse"
./example_analyse.sh