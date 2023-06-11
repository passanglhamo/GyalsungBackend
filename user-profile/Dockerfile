FROM openjdk:8
MAINTAINER erp.microservice.com

RUN mkdir -m 755 -p /home/logs
ADD target/*.jar ROOT.jar

RUN ls -la /opt
ADD stagingCertificate.cer /opt
ADD productionCertificate.cer /opt
RUN keytool --import -alias datahub -file /opt/productionCertificate.cer -keystore "/usr/local/openjdk-8/jre/lib/security/cacerts" -storepass changit

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ROOT.jar"]

#keytool --import -alias callLog -file /opt/ditt-datahub-live-certificate.cer -keystore "/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/security/cacerts"
#keytool --import -alias callLog -file /opt/ditt-datahub-live-certificate.cer -keystore "/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre/lib/security/cacerts"

