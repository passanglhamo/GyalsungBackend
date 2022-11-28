package com.microservice.erp.domain.dto.enrolment;

import com.microservice.erp.domain.dto.EnrolmentCoursesDto;
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
    private Character gender;
    private Character status;
    private Date enrolledOn;
    private Character underAge;
    private List<EnrolmentCoursesDto> enrolmentCoursesDtos;

    public static EnrolmentDto withId(
            BigInteger userId,
            String year,
            Character gender,
            Character status,
            Date enrolledOn,
            Character underAge,
            List<EnrolmentCoursesDto> enrolmentCoursesDtos) {
        return new EnrolmentDto(
                userId,
                year,
                gender,
                status,
                enrolledOn,
                underAge,
                enrolmentCoursesDtos
        );
    }
}
