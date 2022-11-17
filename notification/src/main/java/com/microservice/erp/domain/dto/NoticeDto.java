package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NoticeDto {
    //region private variables
    private Long noticeConfigurationId;
    private String year;
    private String noticeName;
    //endregion
}
