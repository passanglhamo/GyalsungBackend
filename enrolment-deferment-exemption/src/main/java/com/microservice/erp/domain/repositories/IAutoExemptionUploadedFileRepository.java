package com.microservice.erp.domain.repositories;

import com.microservice.erp.domain.entities.AutoExemptionUploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IAutoExemptionUploadedFileRepository extends JpaRepository<AutoExemptionUploadedFile, BigInteger> {
}
