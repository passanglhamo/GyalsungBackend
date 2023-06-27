package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;


@Setter
@Getter
public class GuardianConsentDto {
    //region private variables
    private BigInteger consentIdIdFromUrl;
    private String guardianCidFromUrl;
    //endregion
}
