package com.microservice.erp.domain.dto;

import com.microservice.erp.domain.helper.EnumRole;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Setter
@Getter
public class SaRoleDto {
    private BigInteger roleId;
    private EnumRole roleName;
}
