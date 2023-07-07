package com.microservice.erp.services.iServices;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

public interface IMedicalConfigurationService {
    ResponseEntity<?> readFile(String authHeader,@Valid MedicalExcelCommand command);

    @Getter
    @Setter
    class MedicalExcelCommand {
        private MultipartFile attachedFile;
    }
}
