from cbiit/centos7_base
RUN yum -y update && yum -y install git java-1.8.0-openjdk-devel sudo util-linux wget epel-release \ 
    && yum clean all

WORKDIR /tmp
RUN wget https://archive.apache.org/dist/tomcat/tomcat-9/v9.0.11/bin/apache-tomcat-9.0.11.tar.gz \
    && mkdir apache-tomcat && tar xvfz apache-tomcat-9.0.11.tar.gz -C ./apache-tomcat --strip-components 1 \
    && cd apache-tomcat/webapps && rm -rf examples docs host-manager manager ROOT/*  && cd ../.. \
    && mv apache-tomcat /usr/local/apache-tomcat

RUN wget https://archive.apache.org/dist/maven/maven-3/3.5.4/binaries/apache-maven-3.5.4-bin.tar.gz \
    && tar xvfz apache-maven-3.5.4-bin.tar.gz \
    && mv apache-maven-3.5.4 /opt \
    && ln -s /opt/apache-maven-3.5.4 /opt/apache-maven


ENV PATH=/opt/apache-maven/bin:$PATH \
    JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk \
    WILDFLY_VERSION=8.2.1.Final

RUN sudo groupadd -r wildfly && sudo useradd -r -g wildfly -d /opt/wildfly -s /sbin/nologin wildfly \ 
    && wget https://download.jboss.org/wildfly/$WILDFLY_VERSION/wildfly-$WILDFLY_VERSION.tar.gz -P /tmp \
    && tar xf /tmp/wildfly-$WILDFLY_VERSION.tar.gz -C /opt/ \
    && ln -s /opt/wildfly-$WILDFLY_VERSION /opt/wildfly

COPY run.sh /usr/bin/run.sh
COPY resources/standalone.xml /opt/wildfly/standalone/configuration

RUN mkdir -p /local/content/cdebrowser/modules
COPY resources/ojdbc7-12.1.0.2.0.jar /local/content/cdebrowser/modules
RUN chown -RH wildfly /local/content/cdebrowser/modules

RUN chown -RH wildfly: /usr/bin/run.sh && chmod 700 /usr/bin/run.sh \
    && mkdir /usr/src/cdebrowser && chown -RH wildfly: /usr/src/cdebrowser \
    && chown -RH wildfly: /opt/wildfly
USER wildfly

WORKDIR /usr/src/cdebrowser
RUN git clone https://github.com/CBIIT/cadsr-cde-browser 
EXPOSE 8080
ENTRYPOINT /usr/bin/run.sh  
