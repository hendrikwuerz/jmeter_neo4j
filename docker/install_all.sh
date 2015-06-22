docker build -t ssankara/jmeter-base .
cd jmeter-master
docker build -t ssankara/jmeter .
cd ../jmeter-server
docker build -t ssankara/jmeter-server .