package com.microservice.erp.domain.dto.enrolment;

import lombok.*;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnrolmentDto {
    private BigInteger userId;
    private String year;
    private Character status;
    private Date enrolledOn;
    private List<EnrolmentCoursesDto> enrolmentCoursesDtos;

    public static EnrolmentDto withId(
            BigInteger userId,
            String year,
            Character status,
            Date enrolledOn,
            List<EnrolmentCoursesDto> enrolmentCoursesDtos) {
        return new EnrolmentDto(
                userId,
                year,
                status,
                enrolledOn,
                enrolmentCoursesDtos
        );
    }
}
