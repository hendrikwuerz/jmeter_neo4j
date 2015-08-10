
FROM fedora

# Install the JDK
RUN dnf install -y java
#&& rm -rf /var/cache/yum

# Describe the environment
ENV JDK_VERSION 1.7.0
ENV JMETER_VERSION 2.13



RUN dnf install -y tar

# Install JMeter
RUN cd /var/lib && \
  curl http://psg.mtu.edu/pub/apache/jmeter/binaries/apache-jmeter-$JMETER_VERSION.tgz -o /var/lib/jmeter-$JMETER_VERSION.tgz && \
  tar xf jmeter-$JMETER_VERSION.tgz && \
  rm -f jmeter-$JMETER_VERSION.tgz
 
