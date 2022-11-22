package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Setter
@Getter
public class NoticeDto {
    //region private variables
    private BigInteger noticeConfigurationId;
    private String year;
    private String noticeName;
    //endregion
}
