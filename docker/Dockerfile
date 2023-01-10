FROM tomcat:9.0.70-jdk8-temurin-jammy

RUN apt-get update
RUN apt-get -y install git maven
COPY build.sh /tmp
EXPOSE 8080
ENTRYPOINT "/tmp/build.sh"
