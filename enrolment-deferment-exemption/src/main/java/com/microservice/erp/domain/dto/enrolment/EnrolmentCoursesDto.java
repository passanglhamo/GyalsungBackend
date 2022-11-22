package com.microservice.erp.domain.dto.enrolment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnrolmentCoursesDto {
    //region private variables
    private Long courseId;
    private Long userId;
    private Integer preferenceNumber;
    //endregion

    public static EnrolmentCoursesDto withId(
            Long courseId,
            Long userId,
            Integer preferenceNumber) {
        return new EnrolmentCoursesDto(
                courseId, userId, preferenceNumber
        );
    }
}
