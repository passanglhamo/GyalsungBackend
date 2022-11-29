package com.microservice.erp.domain.helper;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StatusResponse {
    //region private declaration
    private Character status;
    private String message;
    //endregion

    //region empty constructor
    public StatusResponse() {
    }
    //endregion
}
