package com.microservice.erp.services.iServices;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigInteger;

public interface ICreateExemptionService {
    ResponseEntity<?> saveExemption(HttpServletRequest request, CreateExemptionCommand command) throws Exception;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class CreateExemptionCommand {

        private BigInteger id;
        private BigInteger userId;
        private BigInteger reasonId;
        private String approvalRemarks;
        private Character status;
        private String remarks;
        private MultipartFile[] proofDocuments;
    }
}
