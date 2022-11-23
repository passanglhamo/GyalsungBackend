package com.microservice.erp.domain.dto.exemption;

import lombok.Data;

import java.math.BigInteger;

@Data(staticConstructor = "withId")
public class ExemptionFileDto {
    private final BigInteger id;

    private final String filePath;

    private final String fileSize;

    private final String fileName;
}
