package com.microservice.erp.domain.dto;

import com.microservice.erp.domain.helper.EnumRole;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SaRoleDto {
    private Integer roleId;
    private EnumRole roleName;
}
