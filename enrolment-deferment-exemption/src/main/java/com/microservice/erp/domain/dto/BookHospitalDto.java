package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;


@Setter
@Getter
public class BookHospitalDto {
    BigInteger hospitalBookingId;
    Integer dzongkhagId;
    Integer hospitalId;
    BigInteger userId;
    Date screeningDate;
    String hospitalName;
    String dzongkhagName;
    BigInteger hospital_booking_id;
    Integer hospital_id;
    Date screening_date;
    BigInteger user_id;
}
