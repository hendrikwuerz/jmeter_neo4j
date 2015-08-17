#!/bin/bash

docker run \
-v ${JMETER_HOME}/jmeter/logs:/logs \
poolingpeople/jmeter-analyse