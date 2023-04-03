package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Setter
@Getter
public class TaskStatusDto {
    //region private variables
    private BigInteger total_pending_registration;
    private BigInteger total_pending_deferment;
    private BigInteger total_pending_exemption;
    private BigInteger total_deferment_rejected;
    private BigInteger total_exemption_rejected;
    //endregion
}
