#!/bin/bash

# check for existing JMETER_HOME environment variable
if [ -z "$JMETER_HOME" ]; then
  echo "JMETER_HOME is unknown. Set is with export JMETER_HOME=\"/home/myuser/stresstest\""
  exit 1
fi

cd ../
docker build -t poolingpeople/jmeter-base .
cd jmeter-server
docker build -t poolingpeople/jmeter-server .
chmod +x *.sh
cat ./jmeter_master_to_server.pub >> ~/.ssh/authorized_keys