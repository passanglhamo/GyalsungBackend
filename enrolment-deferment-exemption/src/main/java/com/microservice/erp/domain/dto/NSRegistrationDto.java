package com.microservice.erp.domain.dto;

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
public class NSRegistrationDto {
    private BigInteger id;
    private BigInteger userId;
    private Character gender;
    private String year;
    private Date registrationOn;
    private String cid;
    private String fullName;
    public static NSRegistrationDto withId(
            BigInteger id,
            BigInteger userId,
            Character gender,
            String year,
            Date registrationOn,
            String cid,
            String fullName) {
        return new NSRegistrationDto(
                id,
                userId,
                gender,
                year,
                registrationOn,
                cid,
                fullName);
    }

}
