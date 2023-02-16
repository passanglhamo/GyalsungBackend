FROM openjdk:8
MAINTAINER erp.microservice.com

RUN mkdir -m 755 -p /home/logs
ADD target/*.jar ROOT.jar

RUN ls -la /opt
ADD stagingCertificate.cer /opt

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ROOT.jar"]

#keytool --import -alias callLog -file /opt/ditt-datahub-live-certificate.cer -keystore "/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/security/cacerts"
#keytool --import -alias callLog -file /opt/ditt-datahub-live-certificate.cer -keystore "/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/security/cacerts"

