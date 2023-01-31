package com.microservice.erp.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NdiProofResponseDto {
    private String proofUrl;
    private String threadId;
}
