package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class DashboardDto {
    private Integer male_registered;
    private Integer female_registered;
    private Integer male_vregistered;
    private Integer female_vregistered;
    private Integer male_deferred;
    private Integer female_deferred;
    private Integer male_exempted;
    private Integer female_exempted;
}
