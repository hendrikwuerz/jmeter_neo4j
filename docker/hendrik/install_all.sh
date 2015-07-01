chmod +x *.sh
docker build -t hendrik/jmeter-base .
cd jmeter-master
chmod +x *.sh
docker build -t hendrik/jmeter-master .
cd ../jmeter-server
chmod +x *.sh
docker build -t hendrik/jmeter-server .
cat ./jmeter_master_to_server.pub >> ~/.ssh/authorized_keys