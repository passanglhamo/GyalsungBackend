package com.microservice.erp.domain.dto;

import lombok.Data;

import java.math.BigInteger;

/**
 * Created by : Passang Lhamo
 * Creation Date : 25/09/2022
 */
@Data(staticConstructor = "withId")
public class DefermentFileDto {

    private final BigInteger id;

    private final String fileSize;

    private final String fileName;

    private final byte[] file;
}
