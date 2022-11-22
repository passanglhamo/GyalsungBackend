package com.microservice.erp.domain.dto.enrolment;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnrolmentDto {
    private Long userId;
    private Character status;
    private Date enrolledOn;
    private List<EnrolmentCoursesDto> enrolmentCoursesDtos;

    public static EnrolmentDto withId(
            Long userId,
            Character status,
            Date enrolledOn,
            List<EnrolmentCoursesDto> enrolmentCoursesDtos) {
        return new EnrolmentDto(
                userId,
                status,
                enrolledOn,
                enrolmentCoursesDtos
        );
    }
}
