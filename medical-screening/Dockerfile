FROM openjdk:11
MAINTAINER erp.microservice.com

RUN mkdir -m 755 -p /home/logs
ADD target/*.jar ROOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ROOT.jar"]

