package com.microservice.erp.domain.dto.deferment;

import lombok.Data;
/**
 * Created by : Passang Lhamo
 * Creation Date : 25/09/2022
 */
@Data(staticConstructor = "withId")
public class DefermentFileDto {

    private final Long id;

    private final String filePath;

    private final String fileSize;

    private final String fileName;
}
