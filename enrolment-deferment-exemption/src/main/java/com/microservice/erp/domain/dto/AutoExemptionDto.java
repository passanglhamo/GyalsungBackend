package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;

@Setter
@Getter
public class AutoExemptionDto {
    //region private variables
    private MultipartFile attachedFile;
//    private byte[] attachedFileByte;
    //endregion
}
