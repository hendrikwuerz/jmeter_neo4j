cd ../
docker build -t hendrik/jmeter-base .
cd jmeter-master
chmod +x *.sh
docker build -t hendrik/jmeter-master .