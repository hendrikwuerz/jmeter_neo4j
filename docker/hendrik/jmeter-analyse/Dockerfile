FROM java

# copy analyse program
ADD com/ /var/analyse/com

# Create mount point for logs and class files to evaluate log
VOLUME ["/logs"]

ENTRYPOINT java -cp /var/analyse com.poolingpeople.jmeter.analyse.Control /logs/results_tree_success.csv /logs/split/ /logs/min/