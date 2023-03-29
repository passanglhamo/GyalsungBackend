package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;


@Setter
@Getter
public class BookHospitalListDto {
    String hospital_name;
    String full_name;
    String cid;
    Character gender;
    BigInteger hospital_booking_id;
    Integer hospital_id;
    Date screening_date;
    BigInteger user_id;
}
