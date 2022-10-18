package com.microservice.erp.domain.dto.es;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.microservice.erp.domain.helper.DateHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnlistmentScheduleDto {
    private Long id;
    @JsonDeserialize(using = DateHandler.class)
    @NotNull
    @NotEmpty
    private Date fromDate;
    @JsonDeserialize(using = DateHandler.class)
    @NotNull
    @NotEmpty
    private Date toDate;
    @NotNull
    @NotEmpty
    private String status;


}
