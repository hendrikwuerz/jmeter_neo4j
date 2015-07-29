cd ../
docker build -t hendrik/jmeter-base .
cd jmeter-server
docker build -t hendrik/jmeter-server .
chmod +x *.sh
cat ./jmeter_master_to_server.pub >> ~/.ssh/authorized_keys