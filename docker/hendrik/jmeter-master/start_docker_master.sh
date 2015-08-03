docker run \
-p 60000:60000 \
-p 1099:1099 \
-p 4445:4445 \
-v /home/hendrik/jmeter:/scripts \
-v /home/hendrik/jmeter/logs:/logs \
hendrik/jmeter-master \
-n \
-t /scripts/neo4jTest.jmx \
-l /logs/jtl.jtl \
-R$1 \
-Djava.rmi.server.hostname=$(/sbin/ip -o -4 addr list eth0 | awk '{print $4}' | cut -d/ -f1) \
-LDEBUG