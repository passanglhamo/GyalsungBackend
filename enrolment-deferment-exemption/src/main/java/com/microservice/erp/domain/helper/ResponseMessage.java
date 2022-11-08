package com.microservice.erp.domain.helper;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseMessage {
    //region private declaration
    private Integer status;
    private String text;
    private Object dto;
    //endregion

    //region empty constructor
    public ResponseMessage() {
    }
    //endregion
}
