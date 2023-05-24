package com.microservice.erp.domain.dto;

import com.infoworks.lab.rest.validation.Password.PasswordRule;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.math.BigInteger;

@Setter
@Getter
public class ResetPasswordDto {
    //region private variables
    private String email;
    private String domainName;
    private BigInteger requestId;
    @PasswordRule(mixLengthRule = 8, maxLengthRule = 20)
    @NotEmpty(message = "Password must not null or empty!")
    private String password;
    private BigInteger requestIdFromUrl;
    private String emailFromUrl;
    //endregion

}
