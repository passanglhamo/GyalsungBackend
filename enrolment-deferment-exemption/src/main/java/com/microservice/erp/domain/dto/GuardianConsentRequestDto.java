package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;


@Setter
@Getter
public class GuardianConsentRequestDto {
    //region private variables
    BigInteger userId;
    String domainName;
    //endregion
}
