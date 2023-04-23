FROM openjdk:13-jdk-alpine
MAINTAINER wutthipatc
RUN apk update && apk add curl
COPY target/application-0.0.1-SNAPSHOT.jar application-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/application-0.0.1-SNAPSHOT.jar"]