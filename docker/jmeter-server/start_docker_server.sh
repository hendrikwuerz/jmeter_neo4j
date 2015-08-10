#!/bin/bash

if [ -z "$JMETER_HOME" ]; then
echo "JMETER_HOME is unknown. Set is with export JMETER_HOME=\"/home/myuser/stresstest\""
exit 1
fi

docker run \
-p 60000:60000 \
-p 1099:1099 \
-p 4445:4445 \
-d \
-v ${JMETER_HOME}/jmeter:/scripts \
-v ${JMETER_HOME}/jmeter/data:/input_data \
-v ${JMETER_HOME}/jmeter/logs:/logs \
--name jmeter-server \
poolingpeople/jmeter-server \
-Djava.rmi.server.hostname=$(/sbin/ip -o -4 addr list eth0 | awk '{print $4}' | cut -d/ -f1) \
-LDEBUG \
&