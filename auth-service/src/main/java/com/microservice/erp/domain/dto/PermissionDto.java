package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

@Setter
@Getter
public class PermissionDto {
    //region private variables
    private BigInteger roleId;
    private BigInteger permissionId;
    private Integer screenId;
    private Character viewAllowed;
    private Character saveAllowed;
    private Character editAllowed;
    private Character deleteAllowed;
    private List<PermissionListDto> permissionListDtos;
    //endregion
}
