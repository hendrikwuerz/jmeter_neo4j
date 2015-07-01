docker build -t hendrik/jmeter-base .
cd jmeter-server
docker build -t hendrik/jmeter-server .

cat ./jmeter_master_to_server.pub >> ~/.ssh/authorized_keys