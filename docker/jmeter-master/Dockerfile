FROM ssankara/jmeter-base

MAINTAINER Sri Sankaran sri@redhat.com

# Create mount point for script, data and log files
VOLUME ["/scripts"]
VOLUME ["/input_data"]
VOLUME ["/logs"]

# Use a predefined configuration.  This sets the contract for connecting to jmeter servers.
ADD jmeter.properties /var/lib/apache-jmeter-$JMETER_VERSION/bin/

ENTRYPOINT [ "/var/lib/apache-jmeter-2.13/bin/jmeter" ]