package com.microservice.erp.services.iServices.exemption;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface ICreateExemptionService {
    ResponseEntity<?> saveExemption(HttpServletRequest request, CreateExemptionCommand command) throws IOException;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class CreateExemptionCommand {

        private Long id;
        private Long userId;
        private Long reasonId;
        private String approvalRemarks;
        private Character status;
        private String remarks;
        private MultipartFile[] proofDocuments;
    }
}
