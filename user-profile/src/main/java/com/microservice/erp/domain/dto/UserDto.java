package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserDto {

    private String fullName;
    private String password;
    private String email;

    private List<SaRoleDto> saRoleDtos;
}
