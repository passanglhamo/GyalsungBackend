package com.microservice.erp.domain.dto.enrolment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnrolmentCoursesDto {
    //region private variables
    private BigInteger courseId;
    private BigInteger userId;
    private Integer preferenceNumber;
    //endregion

    public static EnrolmentCoursesDto withId(
            BigInteger courseId,
            BigInteger userId,
            Integer preferenceNumber) {
        return new EnrolmentCoursesDto(
                courseId, userId, preferenceNumber
        );
    }
}
