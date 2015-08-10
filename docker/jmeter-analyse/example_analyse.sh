#!/bin/bash

docker run \
-v ${JMETER_HOME}/jmeter/logs:/logs \
hendrik/jmeter-analyse