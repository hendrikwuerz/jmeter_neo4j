docker run \
-p 60000:60000 \
-p 1099:1099 \
-p 4445:4445 \
-v ${JMETER_HOME}/jmeter:/scripts \
-v ${JMETER_HOME}/jmeter/logs:/logs \
poolingpeople/jmeter-master \
-n \
-t /scripts/test.jmx \
-l /logs/jtl.jtl \
-R$1 \
-Djava.rmi.server.hostname=$(/sbin/ip -o -4 addr list eth0 | awk '{print $4}' | cut -d/ -f1) \
-LDEBUG