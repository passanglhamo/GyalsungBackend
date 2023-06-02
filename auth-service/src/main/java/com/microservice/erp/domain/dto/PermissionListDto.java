package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Setter
@Getter
public class PermissionListDto {
    //region private variables
    private BigInteger permission_id;
    private BigInteger role_id;
    private Integer screen_id;
    private Character view_allowed;
    private Character edit_allowed;
    private Character delete_allowed;
    private Character save_allowed;
    private String screen_name;
    private String screen_url;
    private Character userType;
    //endregion

}
