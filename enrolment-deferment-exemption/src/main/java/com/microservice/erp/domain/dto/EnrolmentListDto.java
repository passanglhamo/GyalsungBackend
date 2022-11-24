package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;

@Setter
@Getter
public class EnrolmentListDto {
    //region private variables
    private String academy_name;
    private String full_name;
    private String cid;
    private String dob;
    private BigInteger enrolment_id;
    private BigInteger user_id;
    private Date enrolled_on;
    private String remarks;
    private Character status;
    private Integer training_academy_id;
    private String year;
    private Integer preference_number;
    private BigInteger course_id;
    //endregion
}
