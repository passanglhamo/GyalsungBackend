package com.microservice.erp.domain.dto.deferment;

import lombok.Data;

import java.math.BigInteger;

/**
 * Created by : Passang Lhamo
 * Creation Date : 25/09/2022
 */
@Data(staticConstructor = "withId")
public class DefermentFileDto {

    private final BigInteger id;

    private final String filePath;

    private final String fileSize;

    private final String fileName;
}
