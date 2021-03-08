FROM maven:3.6.3-openjdk-11 AS maven_build
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn package


FROM adoptopenjdk/openjdk11
MAINTAINER baeldung.com
COPY target/customer-service-1.0.0.jar customer-service-1.0.0.jar
ENTRYPOINT ["java","-jar","/customer-service-1.0.0.jar"]