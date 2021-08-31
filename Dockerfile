FROM maven:3.6.3-openjdk-11 AS maven_build
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn package


FROM adoptopenjdk/openjdk11
MAINTAINER baeldung.com
COPY target/customer-service.jar customer-service.jar
ENTRYPOINT ["java","-jar","/customer-service.jar"]