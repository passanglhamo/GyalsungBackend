package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;


@Setter
@Getter
public class DashboardDto {
    private BigInteger total_male_registered;
    private BigInteger total_female_registered;
    private BigInteger total_male_ee;
    private BigInteger total_female_ee;
}
