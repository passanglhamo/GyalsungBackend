package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.yml")
@Getter
@Setter
public class ApplicationProperties {

    @Value("${user.service.check.under.age}")
    private String userCheckUnderAge;

    @Value("${user.service.profile.by.userId}")
    private String userProfileById;

    @Value("${training.management.academy.by.academyId}")
    private String trainingManAcademyByAcademyId;

    @Value("${training.management.course.by.courseId}")
    private String trainingManCourseByCourceId;

    @Value("${user.service.profile.by.userIds}")
    private String userProfileByIds;

    @Value("${user.service.profile.by.cid}")
    private String usersCid;

    @Value("${training.management.hospital.by.hospitalId}")
    private String trainingHospitalById;

    @Value("${training.management.mappedHospital.by.hospitalId}")
    private String trainingMappedHospitalById;
}
