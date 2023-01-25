package com.microservice.erp.domain.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NDIDto {
  private String proofName;
  private ProofAttributeDto proofAttributeDtos;
}
