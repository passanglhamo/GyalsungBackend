package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PermissionDto {
    //region private variables
    private Integer roleId;
    private List<PermissionListDto> permissionListDtos;
    //endregion
}
