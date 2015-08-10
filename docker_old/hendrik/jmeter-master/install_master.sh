cd ../
docker build -t hendrik/jmeter-base .
cd jmeter-master
chmod +x *.sh
chmod 600 jmeter_master_to_server
docker build -t hendrik/jmeter-master .