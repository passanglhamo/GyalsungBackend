package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;

@Setter
@Getter
public class MedicalBookingListDto {
    private BigInteger hospital_schedule_date_id;
    private Date appointment_date;
    private BigInteger hos_schedule_time_id;
    private Date start_time;
    private Date end_time;
    private Character book_status;
    private BigInteger booked_by;
    private String fullName;
}
