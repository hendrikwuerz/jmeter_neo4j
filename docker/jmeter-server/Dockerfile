FROM poolingpeople/jmeter-base

ADD jmeter.properties /var/lib/apache-jmeter-$JMETER_VERSION/bin/

# Expose access to logs & data files
VOLUME ["/scripts"]
VOLUME ["/input_data"]
VOLUME ["/logs"]

# Expose jmeter-server's port (values dicated by those specified in jmeter.properties.
EXPOSE 1099 1099
EXPOSE 4445 4445
EXPOSE 60000 60000

#ENV HOST_IP $(/sbin/ip -o -4 addr list eth0 | awk '{print $4}' | cut -d/ -f1)

# Run jmeter-server 
ENTRYPOINT [ "/var/lib/apache-jmeter-2.13/bin/jmeter-server" ]