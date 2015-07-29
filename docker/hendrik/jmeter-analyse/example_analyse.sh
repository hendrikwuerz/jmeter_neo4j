#!/bin/bash

docker run \
-v /home/hendrik/jmeter/logs:/logs \
-v /home/hendrik/jmeter/result:/result \
hendrik/jmeter-analyse \
/logs/results_tree_success.csv /logs/split/ /logs/min/