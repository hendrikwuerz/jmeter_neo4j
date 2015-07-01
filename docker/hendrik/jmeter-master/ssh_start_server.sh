#!/bin/bash


USERNAME=hendrik
SCRIPT_START="/home/hendrik/docker-jmeter/hendrik/jmeter-server/start_docker_server.sh"
SCRIPT_STOP="/home/hendrik/docker-jmeter/hendrik/jmeter-server/stop_docker_server.sh"


# get passed parameter
while getopts :k:d:t:R: opt
do
	case ${opt} in
		k) KEYFILE=$(readlink -f ${OPTARG}) ;;
		d) DATA_DIR=$(readlink -f ${OPTARG}) ;;
		t) JMX_SCRIPT=$(readlink -f ${OPTARG}) ;;
		R) REMOTE_SERVERS=${OPTARG} ;;
		:) echo "The -${OPTARG} option requires a parameter"
			 exit 1 ;;
		?) echo "Invalid option: -${OPTARG}"
			 exit 1 ;;
	esac
done

echo ""
echo "Starting JMeter test with"
echo "SSH-Key-File:    ${KEYFILE}"
echo "Data dir:        ${DATA_DIR}"
echo "jmx TestPlan:    ${JMX_SCRIPT}"
echo "Remote Servers:  ${REMOTE_SERVERS}"

# split passed ips in array to handle
IFS=',' read -ra array_remote_servers <<< "${REMOTE_SERVERS}"

# copy test-data to serves
echo ""
echo "*********************************"
echo "*** Copy test data to Servers ***"
echo "*********************************"
COUNTER=1
for server in "${array_remote_servers[@]}"; do

    # create working directory on server
    echo "ssh -i \"${KEYFILE}\" -o StrictHostKeyChecking=no -l ${USERNAME} ${server} \"mkdir -p ~/jmeter/data\""
    ssh -i "${KEYFILE}" -o StrictHostKeyChecking=no -l ${USERNAME} ${server} "mkdir -p ~/jmeter/data"

    # copy test data
    echo "scp -i \"${KEYFILE}\" -r ${DATA_DIR}/${COUNTER} ${USERNAME}@${server}:~/jmeter/data"
    scp -i "${KEYFILE}" ${DATA_DIR}/${COUNTER}/* ${USERNAME}@${server}:~/jmeter/data

    # copy test plan
    echo "scp -i \"${KEYFILE}\" ${JMX_SCRIPT} ${USERNAME}@${server}:~/jmeter/$(basename ${JMX_SCRIPT})"
    scp -i "${KEYFILE}" ${JMX_SCRIPT} ${USERNAME}@${server}:~/jmeter/$(basename ${JMX_SCRIPT})

    COUNTER=$[$COUNTER +1]
done




# start docker server on all ips
echo ""
echo "******************************"
echo "*** Starting JMeter Server ***"
echo "******************************"
for server in "${array_remote_servers[@]}"; do
    echo "ssh -i \"${KEYFILE}\" -o StrictHostKeyChecking=no -l ${USERNAME} ${server} \"${SCRIPT_START}\""
    ssh -i "${KEYFILE}" -o StrictHostKeyChecking=no -l ${USERNAME} ${server} "${SCRIPT_START}"
done




# start docker with jmeter master
echo ""
echo "******************************"
echo "*** Starting JMeter Master ***"
echo "******************************"
./start_docker_master.sh




# remove all created containers and test data
echo ""
echo "*******************************"
echo "*** Clean up JMeter Servers ***"
echo "*******************************"
for server in "${array_remote_servers[@]}"; do
    # remove jmeter server docker containers
    echo "ssh -i \"${KEYFILE}\" -o StrictHostKeyChecking=no -l ${USERNAME} ${server} \"${SCRIPT_START}\""
    ssh -i "${KEYFILE}" -o StrictHostKeyChecking=no -l ${USERNAME} ${server} "${SCRIPT_STOP}"

    # remove test files
    echo "ssh -i \"${KEYFILE}\" -o StrictHostKeyChecking=no -l ${USERNAME} ${server} \"rm -r ~/jmeter\""
    ssh -i "${KEYFILE}" -o StrictHostKeyChecking=no -l ${USERNAME} ${server} "rm -r ~/jmeter"
done