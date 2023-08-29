#!/bin/bash

AppVersion="v1.0.41"
DockerHubUser="engrajibkumerghosh"
DockerHubRepoName="ms-training-repo-v1"
DockerHubRepository="${DockerHubUser}/${DockerHubRepoName}"
#
docker login --password "Hema@2020" --username ${DockerHubUser}
####
#FirstServiceDir="first-service"
#FirstServiceName="first-service"
#echo "Creating ${FirstServiceName} Image"
#mvn -pl ${FirstServiceDir} -am clean package -DskipTests
#docker image build -f ${FirstServiceDir}/Dockerfile -t ${FirstServiceName}:${AppVersion} ./${FirstServiceDir}
#docker image tag ${FirstServiceName}:${AppVersion} ${DockerHubRepository}:${FirstServiceName}-${AppVersion}
#docker push ${DockerHubRepository}:${FirstServiceName}-${AppVersion}
##
####
#SecondServiceDir="second-service"
#SecondServiceName="second-service"
#echo "Creating ${SecondServiceName} Image"
#mvn -pl ${SecondServiceDir} -am clean package -DskipTests
#docker image build -f ${SecondServiceName}/Dockerfile -t ${SecondServiceName}:${AppVersion} ./${SecondServiceDir}
#docker image tag ${SecondServiceName}:${AppVersion} ${DockerHubRepository}:${SecondServiceName}-${AppVersion}
#docker push ${DockerHubRepository}:${SecondServiceName}-${AppVersion}
#

###
#AuthServiceDir="auth-service"
#AuthServiceName="auth-service"
#echo "Creating ${AuthServiceName} Image"
#mvn -pl ${AuthServiceDir} -am clean package -DskipTests
#docker image build -f ${AuthServiceName}/Dockerfile -t ${AuthServiceName}:${AppVersion} ./${AuthServiceDir}
#docker image tag ${AuthServiceName}:${AppVersion} ${DockerHubRepository}:${AuthServiceName}-${AppVersion}
#docker push ${DockerHubRepository}:${AuthServiceName}-${AppVersion}
#

####
UserProfileDir="user-profile"
UserProfileName="user-profile"
echo "Creating ${UserProfileName} Image"
mvn -pl ${UserProfileDir} -am clean package -DskipTests
docker image build -f ${UserProfileDir}/Dockerfile -t ${UserProfileName}:${AppVersion} ./${UserProfileDir}
docker image tag ${UserProfileName}:${AppVersion} ${DockerHubRepository}:${UserProfileName}-${AppVersion}
docker push ${DockerHubRepository}:${UserProfileName}-${AppVersion}
##

###
#TrainingManagementDir="training-management"
#TrainingManagementName="training-management"
#echo "Creating ${TrainingManagementName} Image"
#mvn -pl ${TrainingManagementDir} -am clean package -DskipTests
#docker image build -f ${TrainingManagementName}/Dockerfile -t ${TrainingManagementName}:${AppVersion} ./${TrainingManagementDir}
#docker image tag ${TrainingManagementName}:${AppVersion} ${DockerHubRepository}:${TrainingManagementName}-${AppVersion}
#docker push ${DockerHubRepository}:${TrainingManagementName}-${AppVersion}
#

###
#EnrolmentDefermentExemptionDir="enrolment-deferment-exemption"
#EnrolmentDefermentExemptionName="enrolment-deferment-exemption"
#echo "Creating ${EnrolmentDefermentExemptionName} Image"
#mvn -pl ${EnrolmentDefermentExemptionDir} -am clean package -DskipTests
#docker image build -f ${EnrolmentDefermentExemptionName}/Dockerfile -t ${EnrolmentDefermentExemptionName}:${AppVersion} ./${EnrolmentDefermentExemptionDir}
#docker image tag ${EnrolmentDefermentExemptionName}:${AppVersion} ${DockerHubRepository}:${EnrolmentDefermentExemptionName}-${AppVersion}
#docker push ${DockerHubRepository}:${EnrolmentDefermentExemptionName}-${AppVersion}
#

###
#MedicalScreeningDir="medical-screening"
#MedicalScreeningName="medical-screening"
#echo "Creating ${MedicalScreeningName} Image"
#mvn -pl ${MedicalScreeningDir} -am clean package -DskipTests
#docker image build -f ${MedicalScreeningName}/Dockerfile -t ${MedicalScreeningName}:${AppVersion} ./${MedicalScreeningDir}
#docker image tag ${MedicalScreeningName}:${AppVersion} ${DockerHubRepository}:${MedicalScreeningName}-${AppVersion}
#docker push ${DockerHubRepository}:${MedicalScreeningName}-${AppVersion}
#

###
#NotificationDir="notification"
#NotificationName="notification"
#echo "Creating ${NotificationName} Image"
#mvn -pl ${NotificationDir} -am clean package -DskipTests
#docker image build -f ${NotificationName}/Dockerfile -t ${NotificationName}:${AppVersion} ./${NotificationDir}
#docker image tag ${NotificationName}:${AppVersion} ${DockerHubRepository}:${NotificationName}-${AppVersion}
#docker push ${DockerHubRepository}:${NotificationName}-${AppVersion}
#

###
#GatewayServiceDir="gateway-service-config"
#GatewayServiceName="cloud-gateway-service"
#echo "Creating ${GatewayServiceName} Image"
#mvn -pl ${GatewayServiceDir} -am clean package -DskipTests
#docker image build -f ${GatewayServiceDir}/Dockerfile -t ${GatewayServiceName}:${AppVersion} ./${GatewayServiceDir}
#docker image tag ${GatewayServiceName}:${AppVersion} ${DockerHubRepository}:${GatewayServiceName}-${AppVersion}
#docker push ${DockerHubRepository}:${GatewayServiceName}-${AppVersion}
#

####
#EurekaServiceDir="eureka-service-discovery"
#EurekaServiceName="eureka-service-discovery"
#echo "Creating ${EurekaServiceName} Image"
#mvn -pl ${EurekaServiceDir} -am clean package -DskipTests
#docker image build -f ${EurekaServiceDir}/Dockerfile -t ${EurekaServiceName}:${AppVersion} ./${EurekaServiceDir}
#docker image tag ${EurekaServiceName}:${AppVersion} ${DockerHubRepository}:${EurekaServiceName}-${AppVersion}
#docker push ${DockerHubRepository}:${EurekaServiceName}-${AppVersion}
#

###
#MonitoringServiceDir="monitoring"
#MonitoringServiceName="prometheus-db"
#echo "Creating ${MonitoringService} Image"
#docker image build -f ${MonitoringServiceDir}/Dockerfile-embedded -t ${MonitoringServiceName}:${AppVersion} ./${MonitoringServiceDir}
#docker image tag ${MonitoringServiceName}:${AppVersion} ${DockerHubRepository}:${MonitoringServiceName}-${AppVersion}
#docker push ${DockerHubRepository}:${MonitoringServiceName}-${AppVersion}
#
###End-Of-File###