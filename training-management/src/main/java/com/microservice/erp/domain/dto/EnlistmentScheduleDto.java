package com.microservice.erp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnlistmentScheduleDto {
    private BigInteger id;
    //@JsonDeserialize(using = DateHandler.class)
    @NotNull
    @NotEmpty
    private Date fromDate;
    //@JsonDeserialize(using = DateHandler.class)
    @NotNull
    @NotEmpty
    private Date toDate;
    @NotNull
    @NotEmpty
    private String status;


}
