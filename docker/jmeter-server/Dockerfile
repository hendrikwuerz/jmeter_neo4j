FROM ssankara/jmeter-base
MAINTAINER Sri Sankaran sri@redhat.com

ADD jmeter.properties /var/lib/apache-jmeter-$JMETER_VERSION/bin/

# Expose access to logs & data files
VOLUME [ "/logs" ]
VOLUME [ "/input-data" ]

# Expose jmeter-server's port (values dicated by those specified in jmeter.properties.
EXPOSE 1099 60000

# Run jmeter-server 
ENTRYPOINT [ "/var/lib/apache-jmeter-2.13/bin/jmeter-server" ]