package com.microservice.erp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    private BigInteger id;
    private String roleName;
    private Character isOpenUser;

    public static RoleDto withId(
            BigInteger id,
            String roleName,
            Character isOpenUser) {
        return new RoleDto(
                id,
                roleName,
                isOpenUser);
    }

}
