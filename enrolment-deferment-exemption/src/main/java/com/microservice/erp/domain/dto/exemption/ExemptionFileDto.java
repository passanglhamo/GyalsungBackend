package com.microservice.erp.domain.dto.exemption;

import lombok.Data;

@Data(staticConstructor = "withId")
public class ExemptionFileDto {
    private final Long id;

    private final String filePath;

    private final String fileSize;

    private final String fileName;
}
