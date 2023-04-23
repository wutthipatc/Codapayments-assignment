FROM openjdk:13-jdk-alpine
MAINTAINER wutthipatc
RUN apk update && apk add curl
COPY target/round-robin-0.0.1-SNAPSHOT.jar round-robin-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/round-robin-0.0.1-SNAPSHOT.jar"]