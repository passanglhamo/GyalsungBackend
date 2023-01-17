package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Set;

@Getter
@Setter
public class AuthUserDto {
    private BigInteger userId;
    private Object roles;
}
