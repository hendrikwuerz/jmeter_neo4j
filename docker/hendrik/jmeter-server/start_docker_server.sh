docker run \
-p 60000:60000 \
-p 1099:1099 \
-p 4445:4445 \
-v /home/hendrik/jmeter:/scripts \
-v /home/hendrik/jmeter/data/2:/input_data \
-v /home/hendrik/jmeter/logs:/logs hendrik/jmeter-server \
-Djava.rmi.server.hostname=$(/sbin/ip -o -4 addr list eth0 | awk '{print $4}' | cut -d/ -f1) \
-LDEBUG \
&