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
    private BigInteger userId;
    private Character gender;
    private String year;
    private Date registrationOn;
    public static NSRegistrationDto withId(
            BigInteger userId,
            Character gender,
            String year,
            Date registrationOn) {
        return new NSRegistrationDto(
                userId,
                gender,
                year,
                registrationOn);
    }

}
