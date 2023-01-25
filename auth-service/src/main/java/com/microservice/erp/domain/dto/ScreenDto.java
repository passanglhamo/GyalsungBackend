package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Setter
@Getter
public class ScreenDto {
    //region private variables
    private BigInteger screen_group_id;
    private String screen_group_name;
    private String screen_group_icon_name;
    private BigInteger screen_id;
    private String screen_name;
    private String screen_url;
    private String screen_icon_name;
    private Boolean view;
    private Boolean edit;
    private Boolean delete;
    private Boolean save;
    //endregion
}
