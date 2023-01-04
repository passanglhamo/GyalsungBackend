package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Setter
@Getter
public class ResetPasswordDto {
    //region private variables
    private String email;
    private String domainName;
    private BigInteger requestId;
    private String password;
    private BigInteger requestIdFromUrl;
    private String emailFromUrl;
    //endregion

}
