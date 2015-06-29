docker build -t hendrik/jmeter-base .
cd jmeter-master
docker build -t hendrik/jmeter-master .
cd ../jmeter-server
docker build -t hendrik/jmeter-server .